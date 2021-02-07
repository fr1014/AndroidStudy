package com.study.coordinatorlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import com.study.customview.R
import kotlinx.android.synthetic.main.coordinatorlayout_tv.*

class CoordinatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.coordinatorlayout_tv)
//        tv_layout_dependency.setOnClickListener {
//            ViewCompat.offsetTopAndBottom(it,30)
//        }
//        setContentView(R.layout.coordinator_scroll)
        setContentView(R.layout.coordinator_zoomimg)
    }
}