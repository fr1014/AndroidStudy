package com.study.coordinatorlayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.study.customview.R

/**
 * 跟随所依赖的View的Y轴移动
 */
class MoveViewBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    /**
     * 确定是否依赖dependency
     */
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency.id == R.id.tv_layout_dependency
    }

    /**
     * 如果确定依赖dependency，那么child跟随dependency需要做出什么变化（child的位置，大小，状态等）
     */
    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val offsetY = dependency.top - child.top
        ViewCompat.offsetTopAndBottom(child, offsetY)
        return super.onDependentViewChanged(parent, child, dependency)
    }
}