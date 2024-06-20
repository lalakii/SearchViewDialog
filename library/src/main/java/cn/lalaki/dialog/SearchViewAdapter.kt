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

class SearchViewAdapter(
    val ctx: SearchViewDialog,
    var list: MutableList<DataModel>?,
    private val inflater: LayoutInflater,
) :
    RecyclerView.Adapter<SearchViewAdapter.SearchItemHolder>() {
    inner class SearchItemHolder(itemView: View) : ViewHolder(itemView) {
        fun bind(
            dataModel: DataModel,
            pos: Int,
        ) {
            BasePinyinHelper.toHanyuPinyinStringArray(
                itemView.context,
                dataModel.value,
                object :
                    BasePinyinHelper() {
                    override fun onPinyinStringArray(pinyinList: MutableList<Array<String>>) {
                        pinyinList.forEach {
                            dataModel.pinyin += it.joinToString("")
                        }
                        dataModel.pinyin = dataModel.pinyin?.replace(Regex("\\d+"), "")
                    }
                },
            )
            itemView.tag = dataModel
            itemView.setOnClickListener(ctx)
            val tv = itemView.findViewById<TextView>(R.id.item_text)
            tv.text = dataModel.value
            if (dataModel.drawable != null) {
                val drawableSize = ctx.drawableSize
                if (drawableSize != null) {
                    dataModel.drawable?.setBounds(0, 0, drawableSize, drawableSize)
                }
                val padding = ctx.drawablePadding
                if (padding != null) {
                    tv.compoundDrawablePadding = padding
                }
                tv.setCompoundDrawables(dataModel.drawable, null, null, null)
            }
            if (ctx.textColor != 0) {
                tv.setTextColor(ctx.textColor)
            }
            if (ctx.isMultiSelect) {
                val cb = itemView.findViewById<CheckBox>(R.id.item_check)
                cb.visibility = View.VISIBLE
                cb.isChecked = dataModel.isChecked
            } else {
                if (ctx.showRadioIcon) {
                    val rd = itemView.findViewById<RadioButton>(R.id.item_radio)
                    rd.visibility = View.VISIBLE
                    rd.isChecked = dataModel == ctx.radioDefault
                    if (rd.isChecked) {
                        lastSelectIndex = pos
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchItemHolder {
        return SearchItemHolder(inflater.inflate(R.layout.lalaki_dialog_item, parent, false))
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    override fun onBindViewHolder(
        holder: SearchItemHolder,
        position: Int,
    ) {
        if (list != null) {
            holder.bind(list!![position], position)
        }
    }

    var lastSelectIndex: Int = -1

    fun rebind(m: DataModel) {
        val selectedIndex = list?.indexOf(m)
        if (lastSelectIndex == selectedIndex) return
        if (selectedIndex != null) {
            notifyItemChanged(selectedIndex)
            if (lastSelectIndex != -1) {
                notifyItemChanged(lastSelectIndex)
            }
            lastSelectIndex = selectedIndex
        }
    }
}
