package com.study.lottie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.study.customview.R
import kotlinx.android.synthetic.main.activity_lottie.*

class LottieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottie)
        lottie.playAnimation()
        lottie.repeatCount = 100
    }

}