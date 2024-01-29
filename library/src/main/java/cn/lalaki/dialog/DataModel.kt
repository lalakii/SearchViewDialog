package cn.lalaki.dialog

@Suppress("Unused")
class DataModel(var id: Int, var value: String) {
    constructor(id: Int, value: String, isChecked: Boolean) : this(id, value) {
        this.isChecked = isChecked
    }

    var isChecked = false
    var pinyin: MutableList<Array<String>>? = null
}