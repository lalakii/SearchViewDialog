package cn.lalaki.sample.dialog

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.lalaki.dialog.DataModel
import cn.lalaki.dialog.SearchViewDialog

class MainActivity : AppCompatActivity(), SearchViewDialog.OnDataEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        val list = mutableListOf<DataModel>()
        list.add(DataModel("0", "你好"))
        list.add(DataModel("0", "今天"))
        list.add(DataModel("0", "下雪"))
        list.add(DataModel("0", "烤火"))
        list.add(DataModel("0", "冰雹"))
        findViewById<Button>(R.id.show_single_dialog).setOnClickListener {
            val dialog = SearchViewDialog(this)
            dialog.title = "Choose item"
            dialog.data = list.toMutableList()
            dialog.listener = this@MainActivity
            dialog.radioDefault = dialog.data!!.first()
            dialog.textColor = Color.WHITE
            dialog.showRadioIcon = true
            dialog.hideSearchView = true

            dialog.show()
        }
        findViewById<Button>(R.id.show_multi_dialog).setOnClickListener {
            val dialog2 = SearchViewDialog(this)
            dialog2.title = "Choose item"
            dialog2.data = list.toMutableList()
            dialog2.isMultiSelect = true
            dialog2.show()
        }
    }

    override fun onClick(
        dialog: SearchViewDialog,
        tag: Any,
        value: String,
    ) {
        Toast.makeText(this, "Click:$tag,$value", Toast.LENGTH_SHORT).show()
        if (!dialog.showRadioIcon) {
            dialog.dismiss()
        }
    }

    override fun onCheckedChanged(
        dialog: SearchViewDialog,
        checkBox: CompoundButton,
        tag: Any,
        value: String,
        isChecked: Boolean,
    ) {
        Toast.makeText(this, "Click:$tag,$value,$isChecked", Toast.LENGTH_SHORT).show()
        // if need,  dialog.dismiss()
    }

    override fun onCheckedItems(checkedItems: List<DataModel>) {
        Toast.makeText(this, "Count:${checkedItems.size}", Toast.LENGTH_SHORT).show()
    }

    override fun onDismiss() {
    }
}
