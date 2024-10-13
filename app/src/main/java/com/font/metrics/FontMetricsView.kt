package com.font.metrics

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat

class FontMetricsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : View(context, attrs, defStyle) {

    companion object {
        const val DEFAULT_TEXT = "My text line"
        const val DEFAULT_FONT_SIZE_PX = 96
        const val STROKE_WIDTH = 2.0f
    }

    private var mText = DEFAULT_TEXT
    private var mTextSize = 0
    private var mAscentPaint = Paint()
    private var mTopPaint = Paint()
    private var mBaselinePaint = Paint()
    private var mDescentPaint = Paint()
    private var mBottomPaint = Paint()
    private var mMeasuredWidthPaint = Paint()
    private var mTextBoundsPaint = Paint()
    private var mTextPaint = TextPaint()
    private var mLinePaint = Paint()
    private var mRectPaint = Paint()
    private var mBounds: Rect = Rect()
    private var mIsTopVisible = true
    private var mIsAscentVisible = true
    private var mIsBaselineVisible = true
    private var mIsDescentVisible = true
    private var mIsBottomVisible = true
    private var mIsBoundsVisible = true
    private var mIsWidthVisible = true

    init {
        mTextSize = DEFAULT_FONT_SIZE_PX
        mTextPaint.isAntiAlias = true
        mTextPaint.textSize = mTextSize.toFloat()
        mTextPaint.color = Color.BLACK

        mLinePaint.color = Color.RED
        mLinePaint.strokeWidth = STROKE_WIDTH

        mAscentPaint.color = ResourcesCompat.getColor(resources, R.color.ascent, null)
        mAscentPaint.strokeWidth = STROKE_WIDTH

        mTopPaint.color = ResourcesCompat.getColor(resources, R.color.top, null)
        mTopPaint.strokeWidth = STROKE_WIDTH

        mBaselinePaint.color = ResourcesCompat.getColor(resources, R.color.baseline, null)
        mBaselinePaint.strokeWidth = STROKE_WIDTH

        mBottomPaint.color = ResourcesCompat.getColor(resources, R.color.bottom, null)
        mBottomPaint.strokeWidth = STROKE_WIDTH

        mDescentPaint.color = ResourcesCompat.getColor(resources, R.color.descent, null)
        mDescentPaint.strokeWidth = STROKE_WIDTH

        mMeasuredWidthPaint.color =
            ResourcesCompat.getColor(resources, R.color.measured_width, null)
        mMeasuredWidthPaint.strokeWidth = STROKE_WIDTH

        mTextBoundsPaint.color = ResourcesCompat.getColor(resources, R.color.text_bounds, null)
        mTextBoundsPaint.strokeWidth = STROKE_WIDTH
        mTextBoundsPaint.style = Paint.Style.STROKE

        mRectPaint.color = Color.BLACK
        mRectPaint.strokeWidth = STROKE_WIDTH
        mRectPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // center the text baseline vertically
        val verticalAdjustment = this.height / 2
        canvas.translate(0f, verticalAdjustment.toFloat())

        var startX = paddingLeft.toFloat()
        var startY = 0f
        var stopX = this.measuredWidth.toFloat()
        var stopY: Float

        // draw text
        canvas.drawText(mText, startX, startY, mTextPaint) // x=0, y=0

        // draw lines
        startX = 0f

        if (mIsTopVisible) {
            startY = mTextPaint.fontMetrics.top
            stopY = startY
            canvas.drawLine(startX, startY, stopX, stopY, mTopPaint)
        }

        if (mIsAscentVisible) {
            startY = mTextPaint.fontMetrics.ascent
            stopY = startY
            canvas.drawLine(startX, startY, stopX, stopY, mAscentPaint)
        }

        if (mIsBaselineVisible) {
            startY = 0f
            stopY = startY
            canvas.drawLine(startX, startY, stopX, stopY, mBaselinePaint)
        }

        if (mIsDescentVisible) {
            startY = mTextPaint.fontMetrics.descent
            stopY = startY
            canvas.drawLine(startX, startY, stopX, stopY, mDescentPaint)
        }

        if (mIsBottomVisible) {
            startY = mTextPaint.fontMetrics.bottom
            stopY = startY
            mLinePaint.color = Color.RED
            canvas.drawLine(startX, startY, stopX, stopY, mBaselinePaint)
        }

        if (mIsBoundsVisible) {
            mTextPaint.getTextBounds(mText, 0, mText.length, mBounds)
            val dx = paddingLeft.toFloat()
            canvas.drawRect(
                mBounds.left + dx,
                mBounds.top.toFloat(),
                mBounds.right + dx,
                mBounds.bottom.toFloat(),
                mTextBoundsPaint
            )
        }

        if (mIsWidthVisible) {
            // get measured width
            val width = mTextPaint.measureText(mText)

            // get bounding width so that we can compare them
            mTextPaint.getTextBounds(mText, 0, mText.length, mBounds)

            // draw vertical line just before the left bounds
            startX = paddingLeft + mBounds.left - (width - mBounds.width()) / 2
            stopX = startX
            startY = -verticalAdjustment.toFloat()
            stopY = startY + this.height
            canvas.drawLine(startX, startY, stopX, stopY, mMeasuredWidthPaint)

            // draw vertical line just after the right bounds
            startX += width
            stopX = startX
            canvas.drawLine(startX, startY, stopX, stopY, mMeasuredWidthPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = 200
        var height = 200

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthRequirement = MeasureSpec.getSize(widthMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthRequirement
        } else if (widthMode == MeasureSpec.AT_MOST && width > widthRequirement) {
            width = widthRequirement
        }

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightRequirement = MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightRequirement
        } else if (heightMode == MeasureSpec.AT_MOST && width > heightRequirement) {
            height = heightRequirement
        }

        setMeasuredDimension(width, height)
    }

    val fontMetrics: Paint.FontMetrics
        get() = mTextPaint.fontMetrics

    val textBounds: Rect
        get() {
            mTextPaint.getTextBounds(mText, 0, mText.length, mBounds)
            return mBounds
        }

    val measuredTextWidth: Float
        get() = mTextPaint.measureText(mText)

    fun setText(text: String) {
        mText = text
        invalidate()
        requestLayout()
    }

    fun setTextSizeInPixels(pixels: Int) {
        mTextSize = pixels
        mTextPaint.textSize = mTextSize.toFloat()
        invalidate()
        requestLayout()
    }

    fun setTopVisible(isVisible: Boolean) {
        mIsTopVisible = isVisible
        invalidate()
    }

    fun setAscentVisible(isVisible: Boolean) {
        mIsAscentVisible = isVisible
        invalidate()
    }

    fun setBaselineVisible(isVisible: Boolean) {
        mIsBaselineVisible = isVisible
        invalidate()
    }

    fun setDescentVisible(isVisible: Boolean) {
        mIsDescentVisible = isVisible
        invalidate()
    }

    fun setBottomVisible(isVisible: Boolean) {
        mIsBottomVisible = isVisible
        invalidate()
    }

    fun setBoundsVisible(isVisible: Boolean) {
        mIsBoundsVisible = isVisible
        invalidate()
    }

    fun setWidthVisible(isVisible: Boolean) {
        mIsWidthVisible = isVisible
        invalidate()
    }
}
