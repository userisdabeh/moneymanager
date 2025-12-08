package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class DonutView @JvmOverloads constructor (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var ringColor: Int = Color.LTGRAY;
    private var fillColor: Int = Color.GREEN;

    private var progress: Float = 0f

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
        canvas.drawCircle(cx, cy, radius, ringPaint);

        fillPaint.color = fillColor

        val rect = RectF(cx - radius, cy - radius, cx + radius, cy + radius)
        canvas.drawArc(rect, -90f, progress * 360f, true, fillPaint)
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

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, 1f)
        invalidate()
    }
}