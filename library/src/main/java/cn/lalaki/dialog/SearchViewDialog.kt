package cn.lalaki.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import net.sourceforge.pinyin4j.PinyinHelper

class SearchViewDialog(
    private val ctx: Context
) : SearchView.OnQueryTextListener,
    View.OnClickListener, OnDismissListener{
    var data: MutableList<DataModel>? = null
    var title: String? = null
    private val d = DialogEx()
    private var viewList: MutableList<LinearLayout>? = null
    private var dialogView: View
    var isMultiSelect = false
    private var search: SearchView
    private var parent: LinearLayout
    var listener: OnDataEventListener? = null
    private val layout = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    init {
        dialogView = layout.inflate(R.layout.lalaki_custom_dialog, FrameLayout(ctx), false)
        d.setView(dialogView)
        parent = dialogView.findViewById(R.id.lalaki_parent)
        search = dialogView.findViewById(R.id.lalaki_search)
        search.setOnQueryTextListener(this)
        if (listener == null && ctx is OnDataEventListener) {
            listener = ctx
        }
    }

    fun show() {
        if (data != null) {
            viewList = mutableListOf()
            for (item in data!!) {
                item.pinyin = mutableListOf()
                for (c in item.value) {
                    try {
                        item.pinyin!!.add(PinyinHelper.toHanyuPinyinStringArray(c))
                    } catch (_: Exception) {
                    }
                }
                val itemView = layout.inflate(
                    R.layout.lalaki_dialog_item,
                    FrameLayout(ctx),
                    false
                ) as LinearLayout
                itemView.tag = item
                itemView.setOnClickListener(this)
                itemView.findViewById<TextView>(R.id.item_text).text = item.value
                if (isMultiSelect) {
                    val cb = itemView.findViewById<CheckBox>(R.id.item_check)
                    cb.visibility = View.VISIBLE
                    cb.isChecked = item.isChecked
                }
                parent.addView(itemView)
                viewList!!.add(itemView)
            }
        }
        if (!title.isNullOrEmpty()) {
            d.setTitle(title)
        }
        if (isMultiSelect) {
            d.setOnDismissListener(this)
        }
        d.show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrEmpty()) {
            viewList?.forEach {
                if (it.tag is DataModel) {
                    val m = it.tag as DataModel
                    if (m.value.contains(
                            newText,
                            ignoreCase = true
                        )
                    ) {
                        it.visibility = View.VISIBLE
                    } else if (m.pinyin?.any { py1 ->
                            py1.joinToString().contains(newText, ignoreCase = true)
                        } == true) {
                        it.visibility = View.VISIBLE
                    } else {
                        it.visibility = View.GONE
                    }
                }
            }
        } else {
            viewList?.forEach { it.visibility = View.VISIBLE }
        }
        return false
    }

    @Suppress("Unused")
    fun dismiss() {
        d.dismiss()
    }

    override fun onClick(v: View?) {
        if (listener != null && v?.tag is DataModel) {
            val m = v.tag as DataModel
            if (isMultiSelect) {
                val cb = v.findViewById<CheckBox>(R.id.item_check)
                cb.isChecked = !cb.isChecked
                m.isChecked = cb.isChecked
                listener?.onCheckedChanged(this, cb, m.id, m.value, m.isChecked)
            } else {
                listener?.onClick(this, m.id, m.value)
            }
        }
    }

    interface OnDataEventListener {
        fun onClick(dialog: SearchViewDialog, id: Int, value: String)
        fun onCheckedChanged(
            dialog: SearchViewDialog,
            checkBox: CompoundButton,
            id: Int,
            value: String,
            isChecked: Boolean
        )

        fun onCheckedItems(checkedItems: List<DataModel>)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        val checkedItems = viewList?.map {
            it.tag as DataModel
        }?.filter { it.isChecked }
        if (checkedItems != null) {
            listener?.onCheckedItems(checkedItems)
        }
    }

    inner class DialogEx : AlertDialog(ctx)
}