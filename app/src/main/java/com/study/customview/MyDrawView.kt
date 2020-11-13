package com.study.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MyDrawView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), LifecycleObserver {
    private var mWidth = 0f
    private var mHeight = 0f
    private var mRadius = 0f
    private var mAngle = 10f
    private var rotationJob: Job? = null
    private var sineWaveSamplesPath = Path()

    private val textPaint = Paint().apply {
        typeface = Typeface.DEFAULT_BOLD
        textSize = 50f
        color = context.getColor(R.color.colorWhite)
    }

    private val fillCirclePaint = Paint().apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.colorWhite)
    }

    private val solidLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = context.getColor(R.color.colorWhite)
    }

    private val vectorPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = context.getColor(R.color.colorAccent)
    }

    private val dashedLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        //设置画虚线floatArrayOf(10f,10f) 长度为10f 间隔为qof
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        strokeWidth = 5f
        color = context.getColor(R.color.colorYellow)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        mRadius = if (mWidth > mHeight / 2) mHeight / 4 else mWidth / 2
        mRadius -= 20f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawAxises(this)
            drawCircle(this)
            drawVector(this)
            drawProjections(this)
            drawSineWave(this)
        }
    }

    private fun drawAxises(canvas: Canvas) {
//        canvas.save()
//        canvas.translate()
//        canvas.drawLine()
//        canvas.restore()
        /**
         * 将画布移动到某个位置为原点再进行绘制
         * 该函数是上述函数的封装
         */
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, solidLinePaint)
            drawLine(0f, -mHeight / 2, 0f, mHeight / 2, solidLinePaint)
        }

        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, solidLinePaint)
        }
    }

    private fun drawCircle(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawCircle(
                0f,
                0f,
                mRadius,
                dashedLinePaint
            )
        }
    }

    private fun drawVector(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            withRotation(-mAngle) {
                drawLine(0f, 0f, mRadius, 0f, vectorPaint)
            }
        }
    }

    //增加角度--旋转
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startRotating() {
        rotationJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(100)
                mAngle += 5f
                invalidate()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseRotating() {
        rotationJob?.cancel()
    }

    //两点 与 两点之间的实线、虚线
    private fun drawProjections(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            drawCircle(mRadius * cos(mAngle.toRadians()), 0f, 20f, fillCirclePaint)
        }
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawCircle(mRadius * cos(mAngle.toRadians()), 0f, 20f, fillCirclePaint)
        }

        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            val x = mRadius * cos(mAngle.toRadians())
            val y = mRadius * sin(mAngle.toRadians())

            withTranslation(x, -y) {
                drawLine(0f, 0f, 0f, y, solidLinePaint)
                drawLine(0f, 0f, 0f, -mHeight / 4 + y, dashedLinePaint)
            }
        }
    }

    //正弦函数图像
    private fun drawSineWave(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            val samplesCount = 50
            val dy = mHeight / 2 / samplesCount
            sineWaveSamplesPath.reset()
            sineWaveSamplesPath.moveTo(mRadius * cos(mAngle.toRadians()), 0f)
            repeat(samplesCount) {
                val x = mRadius * cos(it * -0.15 + mAngle.toRadians())
                val y = -dy * it
                sineWaveSamplesPath.quadTo(x.toFloat(), y, x.toFloat(), y)
            }
            drawPath(sineWaveSamplesPath, vectorPaint)
            drawTextOnPath("Hello World", sineWaveSamplesPath, 1000f, -20f, textPaint)
        }
    }

    private fun Float.toRadians() = this / 180 * PI.toFloat();
}