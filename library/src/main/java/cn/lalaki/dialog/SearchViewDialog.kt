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
import java.util.Locale

@Suppress("MemberVisibilityCanBePrivate")
class SearchViewDialog(
    private val ctx: Context,
) : SearchView.OnQueryTextListener, View.OnClickListener, OnDismissListener {
    var radioDefault: DataModel? = null
    var drawablePadding: Int? = null
    var drawableSize: Int? = null
    var data: List<DataModel>? = null
    var autoRecycleDrawable = true
    var autoRecycleCheckedDrawable = false
    var showRadioIcon = false
    var hideSearchView = false
    var title: String? = null
    var textColor = 0
    var searchViewBackColor = 0
    private var d: DialogEx? = DialogEx()
    var isMultiSelect = false
    private var search: SearchView
    private var parent: RecyclerView
    var listener: OnDataEventListener? = null
    var customView: View? = null
    var searchHint = 0
    var endTextRes = 0
    var needPinyin = true
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
            d!!.findViewById<FrameLayout>(R.id.lalaki_search_bg).visibility = View.GONE
        } else {
            if (searchViewBackColor != 0) {
                search.setBackgroundColor(searchViewBackColor)
                d!!.findViewById<FrameLayout>(R.id.lalaki_search_bg)
                    .setBackgroundColor(searchViewBackColor)
            }
            if (searchHint != 0) {
                search.queryHint = d!!.context.getString(searchHint)
            }
            search.setOnQueryTextListener(this)
        }
        if (data != null) {
            if (endTextRes != 0) {
                val endView = d!!.findViewById<TextView>(R.id.end_view)
                endView.setText(endTextRes)
                endView.visibility = View.VISIBLE
            }
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
        val query = newText?.trim()
        if (!query.isNullOrEmpty()) {
            val lowerQuery = query.lowercase(Locale.getDefault())
            val pinyinQuery = lowerQuery.replace(" ", "")
            val filteredList = data?.filter { item ->
                item.value.lowercase(Locale.getDefault()).contains(lowerQuery) ||
                        item.pinyin?.lowercase(Locale.getDefault())?.contains(pinyinQuery) == true
            }
            adapter?.list = filteredList
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

        for (it in list) {
            if (!autoRecycleCheckedDrawable && it === radioDefault) {
                continue
            }
            val drawable = it.drawable
            if (drawable is BitmapDrawable) {
                val dBmp = drawable.bitmap
                if (dBmp != null && !dBmp.isRecycled) {
                    dBmp.recycle()
                }
            }
        }
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
            val currentData = data
            if (currentData != null) {
                val checkedItems = currentData.filter { it.isChecked }
                if (checkedItems.isNotEmpty()) {
                    data = if (autoRecycleCheckedDrawable) {
                        checkedItems
                    } else {
                        currentData.filter { !it.isChecked }
                    }
                    listener?.onCheckedItems(checkedItems)
                }
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