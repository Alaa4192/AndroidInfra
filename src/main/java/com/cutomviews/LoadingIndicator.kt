package com.cutomviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.infrastructure.extensions.dp

class LoadingIndicator(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
    : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0, 0)
    constructor(context: Context?) : this(context, null, 0, 0)

    private var isStart = false
    private var startAngel = 0f
    private var innerStartAngel = 0f
    private var outerStartAngel = 0f
    private var sweepAngel = 90f
    private var innerSweepAngel = 90f
    private var outerSweepAngel = 90f
    private val space = dp(8)
    private val doubleSpace = space * 2
    private val innerCircleStrokeWidth = dp(3)
    private val circleStrokeWidth = dp(2)
    private val outerCircleStrokeWidth = dp(1)
    private val defaultSize = dp(40).toInt()

    init {
        startThread()
    }

    private fun startThread() {
        Thread {
            while (isStart) {
                Thread.sleep(80)
                startAngel = (startAngel + 20) % 360
                sweepAngel = (sweepAngel + 15) % 360

                innerStartAngel = (innerStartAngel - 15) % 360
                innerSweepAngel = (innerSweepAngel - 10) % 360

                outerStartAngel = (outerStartAngel + 10) % 360
                outerSweepAngel = (outerSweepAngel + 5) % 360

                invalidate()
            }

        }.start()
    }

    private val innerArcPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = innerCircleStrokeWidth
    }

    private val arcPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = circleStrokeWidth
    }

    private val outerArcPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = outerCircleStrokeWidth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (widthMeasureSpec <= 0 || heightMeasureSpec <= 0) {
            setMeasuredDimension(defaultSize, defaultSize)
        } else {
            val minSize = Math.min(widthMeasureSpec, heightMeasureSpec)
            super.onMeasure(minSize, minSize)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = measuredWidth.toFloat()
        val height = measuredHeight.toFloat()

        canvas?.drawArc(doubleSpace, doubleSpace, width - doubleSpace, height - doubleSpace, innerStartAngel, innerSweepAngel, false, innerArcPaint)
        canvas?.drawArc(space, space, width - space, height - space, startAngel, sweepAngel, false, arcPaint)
        canvas?.drawArc(outerCircleStrokeWidth, outerCircleStrokeWidth, width - outerCircleStrokeWidth, height - outerCircleStrokeWidth, outerStartAngel, outerSweepAngel, false, outerArcPaint)
    }

    fun setProgressColor(progressColor: Int) {
        innerArcPaint.color = progressColor
        arcPaint.color = progressColor
        outerArcPaint.color = progressColor

        invalidate()
    }

    fun start() {
        isStart = true
        startThread()
    }

    fun stop() {
        isStart = false
    }
}