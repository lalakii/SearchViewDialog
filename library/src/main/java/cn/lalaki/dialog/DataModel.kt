package cn.lalaki.dialog

import android.graphics.drawable.Drawable

@Suppress("Unused")
open class DataModel(var tag: Any, var value: String) {
    constructor(tag: Any, value: String, isChecked: Boolean) : this(tag, value) {
        this.isChecked = isChecked
    }

    constructor(tag: Any, value: String, drawable: Drawable?, isChecked: Boolean) : this(
        tag,
        value,
    ) {
        this.isChecked = isChecked
        this.drawable = drawable
    }

    constructor(tag: Any, value: String, drawable: Drawable?) : this(
        tag,
        value,
    ) {
        this.drawable = drawable
    }

    var drawable: Drawable? = null
    var pinyin: String? = null
    var isChecked = false
}
