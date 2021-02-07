package com.study.coordinatorlayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 * 跟随所依赖的View滚动
 *
 * onStartNestedScroll：是否要让Behavior处理滑动，处理返回true，不处理返回false，如果返回为false则不走Behavior剩下的方法。
 * onNestedPreScroll：具体怎么处理滑动的方法。
 */
class ScrollerViewBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    /*
        onStartNestedScroll：当手指按下屏幕的时候触发，用来决定是否要让Behavior处理这次滑动，true为处理，
        false为不处理，如果不处理，那么Behavior的后续方法也就不会在再调用了，方法中也提供了一些辅助参数，
        比如type，可以用来判断用户动作，比如是TYPE_TOUCH按住屏幕拖动，TYPE_NON_TOUCH快速拉动屏幕等。
     */
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return true
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        //target（被依赖View）
        val scrollY = target.scrollY
        child.scrollY = scrollY
    }
}