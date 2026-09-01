package cn.lalaki.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.sourceforge.pinyin4j.BasePinyinHelper
import kotlin.concurrent.thread

class SearchViewAdapter(
    val ctx: SearchViewDialog,
    var list: List<DataModel>?,
    private val inflater: LayoutInflater,
) : RecyclerView.Adapter<SearchViewAdapter.SearchItemHolder>() {
    var lastSelectIndex: Int = -1

    inner class SearchItemHolder(itemView: View) : ViewHolder(itemView) {
        private val tv = itemView.findViewById<TextView>(R.id.item_text)
        private val cb = itemView.findViewById<CheckBox>(R.id.item_check)
        private val rd = itemView.findViewById<RadioButton>(R.id.item_radio)

        fun bind(dataModel: DataModel, pos: Int) {
            val pinyin = dataModel.pinyin
            if (pinyin == null && ctx.needPinyin) {
                thread {
                    val pinyinBuilder = StringBuilder()
                    val firstCharsBuilder = StringBuilder()
                    for (it in dataModel.value) {
                        val pinyinIt =
                            BasePinyinHelper.toHanyuPinyinStringArray(itemView.context, it)
                        if (pinyinIt.isNotEmpty()) {
                            val pinyin1 = pinyinIt.first()
                            pinyinBuilder.append(pinyin1)
                            if (pinyin1.isNotEmpty()) {
                                firstCharsBuilder.append(pinyin1.first())
                            }
                        }
                        pinyinBuilder.append(firstCharsBuilder)
                    }
                    dataModel.pinyin = pinyinBuilder.toString().replace(Regex("\\d+"), "")
                }
            }
            itemView.tag = dataModel
            itemView.setOnClickListener(ctx)
            tv.text = dataModel.value + dataModel.pinyin

            val drawable = dataModel.drawable
            if (drawable != null) {
                val drawableSize = ctx.drawableSize
                if (drawableSize != null) {
                    drawable.setBounds(0, 0, drawableSize, drawableSize)
                }
                val padding = ctx.drawablePadding
                if (padding != null) {
                    tv.compoundDrawablePadding = padding
                }
                tv.setCompoundDrawables(drawable, null, null, null)
            } else {
                tv.setCompoundDrawables(null, null, null, null)
            }

            if (ctx.textColor != 0) {
                tv.setTextColor(ctx.textColor)
            }

            if (ctx.isMultiSelect) {
                cb.visibility = View.VISIBLE
                cb.isChecked = dataModel.isChecked
                rd.visibility = View.GONE
            } else {
                cb.visibility = View.GONE
                if (ctx.showRadioIcon) {
                    rd.visibility = View.VISIBLE
                    val isCurrentSelected = dataModel == ctx.radioDefault
                    rd.isChecked = isCurrentSelected
                    if (isCurrentSelected) {
                        lastSelectIndex = pos
                    }
                } else {
                    rd.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemHolder {
        return SearchItemHolder(inflater.inflate(R.layout.lalaki_dialog_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: SearchItemHolder, position: Int) {
        val finalList = list
        if (finalList != null) {
            holder.bind(finalList[position], position)
        }
    }

    fun rebind(m: DataModel) {
        val selectedIndex = list?.indexOf(m) ?: -1
        if (selectedIndex == -1 || lastSelectIndex == selectedIndex) return
        notifyItemChanged(selectedIndex)
        if (lastSelectIndex != -1 && lastSelectIndex < itemCount) {
            notifyItemChanged(lastSelectIndex)
        }
        lastSelectIndex = selectedIndex
    }
}