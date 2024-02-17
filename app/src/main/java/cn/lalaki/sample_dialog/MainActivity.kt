package cn.lalaki.sample_dialog

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Toast
import cn.lalaki.dialog.DataModel
import cn.lalaki.dialog.SearchViewDialog

class MainActivity : Activity(), SearchViewDialog.OnDataEventListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val list = mutableListOf<DataModel>()
        list.add(DataModel("","Hello",false))
        list.add(DataModel("","测试数据",false))
        list.add(DataModel("","你好",true))
        list.add(DataModel("","Example",false))

        findViewById<Button>(R.id.show_single_dialog).setOnClickListener {
            val dialog = SearchViewDialog(this@MainActivity)
            dialog.data = list
            dialog.title = "Choose language"
            dialog.listener = this@MainActivity
            dialog.textColor = Color.WHITE
            dialog.show()
        }
        findViewById<Button>(R.id.show_multi_dialog).setOnClickListener {
            val dialog = SearchViewDialog(this)
            dialog.data = list
            dialog.title = "Choose language"
            dialog.isMultiSelect = true
            dialog.show()
        }
    }

    override fun onClick(dialog: SearchViewDialog, tag: Any, value: String) {
        Toast.makeText(this, "Click:${tag},${value}", Toast.LENGTH_SHORT).show()
        // if need,  dialog.dismiss()
    }

    override fun onCheckedChanged(
        dialog: SearchViewDialog,
        checkBox: CompoundButton,
        tag: Any,
        value: String,
        isChecked: Boolean
    ) {
        Toast.makeText(this, "Click:${tag},${value},${isChecked}", Toast.LENGTH_SHORT).show()
        // if need,  dialog.dismiss()
    }

    override fun onCheckedItems(checkedItems: List<DataModel>) {
        Toast.makeText(this, "Count:${checkedItems.size}", Toast.LENGTH_SHORT).show()
    }
}