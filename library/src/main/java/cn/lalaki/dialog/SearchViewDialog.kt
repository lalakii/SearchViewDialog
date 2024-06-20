package cn.lalaki.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

@Suppress("MemberVisibilityCanBePrivate")
class SearchViewDialog(
    private val ctx: Context,
) : SearchView.OnQueryTextListener, View.OnClickListener, OnDismissListener {
    var radioDefault: DataModel? = null
    var drawablePadding: Int? = null
    var drawableSize: Int? = null
    var data: MutableList<DataModel>? = null
    var autoRecycleDrawable = true
    var autoRecycleCheckedDrawable = false
    var showRadioIcon = false
    var hideSearchView = false
    var title: String? = null
    var textColor = 0
    private var d: DialogEx? = DialogEx()
    var isMultiSelect = false
    private var search: SearchView
    private var parent: RecyclerView
    var listener: OnDataEventListener? = null
    var customView: View? = null
    private var adapter: SearchViewAdapter? = null

    init {
        d!!.setContentView(
            d!!.layoutInflater.inflate(
                R.layout.lalaki_custom_dialog,
                FrameLayout(ctx),
                false,
            ),
        )
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(d!!.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        d!!.window?.attributes = lp
        parent = d!!.findViewById(R.id.lalaki_parent)
        search = d!!.findViewById(R.id.lalaki_search)
        if (listener == null && ctx is OnDataEventListener) {
            listener = ctx
        }
    }

    fun show() {
        if (d == null) {
            return
        }
        if (customView != null) {
            parent.visibility = View.GONE
            val view = parent.parent as ViewGroup
            view.removeAllViews()
            view.addView(customView)
        }
        if (hideSearchView) {
            search.visibility = View.GONE
        } else {
            search.setOnQueryTextListener(this)
        }
        if (data != null) {
            adapter = SearchViewAdapter(this, data, d!!.layoutInflater)
            parent.adapter = adapter
            if (!title.isNullOrEmpty()) {
                val view = d!!.findViewById<TextView>(R.id.lalaki_title)
                view.text = title
                if (textColor != 0) {
                    view.setTextColor(textColor)
                }
            }
        }
        d!!.setOnDismissListener(this)
        d!!.show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    @Suppress("NotifyDataSetChanged")
    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText?.trim()?.isNotEmpty() == true) {
            val newList = data?.toMutableList()
            adapter?.list =
                newList?.filter {
                    if (it.value.contains(
                            newText,
                            ignoreCase = true,
                        )
                    ) {
                        true
                    } else {
                        it.pinyin?.contains(newText, ignoreCase = true) == true
                    }
                }?.toMutableList()
        } else {
            adapter?.list = data
        }
        adapter?.notifyDataSetChanged()
        return false
    }

    @Suppress("Unused")
    fun dismiss() {
        d?.dismiss()
    }

    @Suppress("Unused")
    private fun recycle() {
        if (!autoRecycleDrawable) return
        val list = adapter?.list ?: return
        if (!autoRecycleCheckedDrawable && radioDefault != null) {
            list.remove(radioDefault)
        }
        for (it in list) {
            val drawable = it.drawable
            if (drawable is BitmapDrawable && !drawable.bitmap.isRecycled) {
                drawable.bitmap?.recycle()
            }
        }
        adapter?.list?.clear()
        adapter?.list = null
        adapter = null
        d?.window?.decorView?.visibility = View.GONE
        d = null
        data = null
    }

    override fun onClick(v: View?) {
        if (listener != null && v?.tag is DataModel) {
            val m = v.tag as DataModel
            if (isMultiSelect) {
                val cb = v.findViewById<CheckBox>(R.id.item_check)
                cb.isChecked = !cb.isChecked
                m.isChecked = cb.isChecked
                listener?.onCheckedChanged(this, cb, m.tag, m.value, m.isChecked)
            } else {
                radioDefault = m
                adapter?.rebind(m)
                listener?.onClick(this, m.tag, m.value)
            }
        }
    }

    interface OnDataEventListener {
        fun onClick(
            dialog: SearchViewDialog,
            tag: Any,
            value: String,
        )

        fun onCheckedChanged(
            dialog: SearchViewDialog,
            checkBox: CompoundButton,
            tag: Any,
            value: String,
            isChecked: Boolean,
        )

        fun onCheckedItems(checkedItems: List<DataModel>)

        fun onDismiss() {
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        if (isMultiSelect) {
            val checkedItems = data?.filter { it.isChecked }
            if (checkedItems != null) {
                if (autoRecycleCheckedDrawable) {
                    data = checkedItems.toMutableList()
                } else {
                    data?.removeAll(checkedItems)
                }
                listener?.onCheckedItems(checkedItems)
            }
        }
        recycle()
        listener?.onDismiss()
    }

    inner class DialogEx : Dialog(ctx) {
        override fun setContentView(view: View) {
            d!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            super.setContentView(view)
        }
    }
}
