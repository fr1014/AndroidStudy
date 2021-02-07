package com.study.palette

import android.graphics.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.study.customview.R
import kotlinx.android.synthetic.main.activity_platte.*


class PaletteActivity : AppCompatActivity() {
    private var mCanvas: Canvas? = null
    private var mPaint: Paint? = null
    private var bgBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_platte)

        Glide.with(this)
            .asBitmap()
            .apply(RequestOptions().override(200, 200))
            .load("https://s3.ax1x.com/2021/01/28/y9nQUJ.jpg")
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap?,
                    transition: Transition<in Bitmap>?
                ) {
                    resource?.let {
                        iv_img.setImageBitmap(it)
                        Palette.from(it).generate(object : Palette.PaletteAsyncListener {
                            override fun onGenerated(palette: Palette?) {
                                //记得判空
                                if (palette == null) return;
                                when {
                                    palette.getVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                                        createLinearGradientBitmap(
                                            palette.getLightVibrantColor(Color.TRANSPARENT),
                                            palette.getVibrantColor(
                                                Color.TRANSPARENT
                                            )
                                        )
                                    }
                                    palette.getDarkVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                                        createLinearGradientBitmap(
                                            palette.getDarkVibrantColor(Color.TRANSPARENT),
                                            palette.getVibrantColor(
                                                Color.TRANSPARENT
                                            )
                                        )
                                    }
                                    palette.getDarkMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                                        createLinearGradientBitmap(
                                            palette.getDarkMutedColor(Color.TRANSPARENT),
                                            palette.getMutedColor(
                                                Color.TRANSPARENT
                                            )
                                        )
                                    }
                                    else -> {
                                        createLinearGradientBitmap(
                                            palette.getLightMutedColor(Color.TRANSPARENT),
                                            palette.getLightVibrantColor(
                                                Color.TRANSPARENT
                                            )
                                        )
                                    }
                                }
                            }

                        })
                    }
                }

            })

    }

    //创建线性渐变背景色
    private fun createLinearGradientBitmap(darkColor: Int, color: Int) {
        val bgColors = IntArray(2)
        bgColors[0] = darkColor
        bgColors[1] = color
        if (bgBitmap == null) {
            bgBitmap =
                Bitmap.createBitmap(iv_bg.width, iv_bg.height, Bitmap.Config.ARGB_8888)
        }
        if (mCanvas == null) {
            mCanvas = Canvas()
        }
        if (mPaint == null) {
            mPaint = Paint()
        }
        mCanvas?.setBitmap(bgBitmap)
        mCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        val gradient: LinearGradient? = bgBitmap?.height?.let {
            LinearGradient(
                0f, 0f, 0f,
                it.toFloat(), bgColors[0], bgColors[1], Shader.TileMode.CLAMP
            )
        }
        mPaint?.shader = gradient
        val rectF =
            bgBitmap?.width?.toFloat()?.let { RectF(0f, 0f, it, bgBitmap!!.height.toFloat()) }
        if (rectF != null) {
            mCanvas?.drawRect(rectF, mPaint!!)
        }
        iv_bg.setImageBitmap(bgBitmap)
    }
}