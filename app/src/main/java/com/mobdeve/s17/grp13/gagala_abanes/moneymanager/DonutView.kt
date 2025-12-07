package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class DonutView @JvmOverloads constructor (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var ringColor: Int = Color.LTGRAY;
    private var fillColor: Int = Color.GREEN;

    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var ringWidth = 40f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width/2f
        val cy = height/2f
        val radius = min(width, height) / 2f - ringWidth

        ringPaint.color = ringColor
        ringPaint.strokeWidth = ringWidth

        fillPaint.color = fillColor

        canvas.drawCircle(cx, cy, radius, ringPaint);
        canvas.drawCircle(cx, cy, radius - (ringWidth / 2f), fillPaint)
    }

    fun setColors(ring: Int, fill: Int) {
        ringColor = ring
        fillColor = fill
        invalidate()
    }

    fun setRingWidth(dp: Float) {
        ringWidth = dp * resources.displayMetrics.density
        invalidate()
    }
}