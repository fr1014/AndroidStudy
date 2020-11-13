package com.study.customview

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

/**
 * 自定义EditTextView
 * 1.自定义属性 attrs.xml
 */
class EditTextWithClear @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var iconDrawable: Drawable? = null

    init {
        //初始化自定义属性
        context.theme.obtainStyledAttributes(attrs, R.styleable.EditTextWithClear, 0, 0)
            .apply {
                try {
                    val iconId = getResourceId(
                        R.styleable.EditTextWithClear_icon_id,
                        R.drawable.ic_baseline_clear_24
                    )
                    iconDrawable = ContextCompat.getDrawable(context, iconId)
                } finally {
                    recycle()
                }
            }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        toggleClearIcon()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { e ->
            iconDrawable?.let {
                /**
                 * e.action == MotionEvent.ACTION_UP    只有手离开屏幕时才触发
                 * e.x > width - it.intrinsicWidth      判断点击位置是否在图标位置
                 * e.x < width
                 * e.y > height / 2 - it.intrinsicHeight / 2
                 * e.y < height / 2 + it.intrinsicHeight / 2
                 */
                if (e.action == MotionEvent.ACTION_UP
                    && e.x > width - it.intrinsicWidth
                    && e.x < width
                    && e.y > height / 2 - it.intrinsicHeight / 2
                    && e.y < height / 2 + it.intrinsicHeight / 2
                ) {
                    text?.clear()
                }
            }
        }

        performClick() //处理点击事件

        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        toggleClearIcon()
    }

    private fun toggleClearIcon() {
        val icon = if (isFocused && text?.isNotEmpty() == true) iconDrawable else null
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon, null)
    }
}