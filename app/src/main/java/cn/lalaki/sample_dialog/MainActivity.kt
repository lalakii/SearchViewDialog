package cn.lalaki.sample_dialog

import android.app.Activity
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
        list.add(DataModel(1, "Java"))
        list.add(DataModel(2, "Kotlin"))
        list.add(DataModel(3, "易语言"))
        list.add(DataModel(4, "Perl", true))
        list.add(DataModel(5, "Delphi"))
        list.add(DataModel(6, "文言文", true))
        list.add(DataModel(7, "Shell", false))

        findViewById<Button>(R.id.show_single_dialog).setOnClickListener {
            val dialog = SearchViewDialog(this)
            dialog.data = list
            dialog.title = "Choose language"
            dialog.listener = this
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

    override fun onClick(dialog: SearchViewDialog, id: Int, value: String) {
        Toast.makeText(this, "Click:${id},${value}", Toast.LENGTH_SHORT).show()
        // if need,  dialog.dismiss()
    }

    override fun onCheckedChanged(
        dialog: SearchViewDialog,
        checkBox: CompoundButton,
        id: Int,
        value: String,
        isChecked: Boolean
    ) {
        Toast.makeText(this, "Click:${id},${value},${isChecked}", Toast.LENGTH_SHORT).show()
        // if need,  dialog.dismiss()
    }

    override fun onCheckedItems(checkedItems: List<DataModel>) {
        Toast.makeText(this, "Count:${checkedItems.size}", Toast.LENGTH_SHORT).show()
    }
}