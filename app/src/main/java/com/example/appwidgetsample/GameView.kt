package com.example.appwidgetsample

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView
import java.lang.Exception

class GameView: SurfaceView, Runnable {

    private var mContext = context
    private var mSurfaceHolder = holder
    private val mPaint = Paint().apply { color = Color.DKGRAY }
    private val mPath = Path()
    private var mBitmapX: Int = 0
    private var mBitmapY: Int = 0
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0
    private lateinit var mBitmap: Bitmap
    private lateinit var mWinnerRect: RectF
    private var mRunning: Boolean = false
    private lateinit var mGameThread: Thread
    private lateinit var mFlashlightCone: FlashlightCone

    constructor(context: Context,
                attrs: AttributeSet? = null,
                defStyleAttr: Int = 0): super(context, attrs, defStyleAttr) {
        initThis(context)
    }

    private fun initThis(context: Context) {
        mContext = context
        mSurfaceHolder = holder
    }

    override fun run() {
        var canvas: Canvas
        while (mRunning) {
            if (mSurfaceHolder.surface.isValid) {
                val x: Int = mFlashlightCone.mX
                val y: Int = mFlashlightCone.mY
                val radius: Int = mFlashlightCone.mRadius
                try {
                    canvas = mSurfaceHolder.lockCanvas()
                    canvas.save()
                    canvas.drawColor(Color.WHITE)
                    canvas.drawBitmap(mBitmap, mBitmapX.toFloat(),
                        mBitmapY.toFloat(), mPaint)
                    mPath.addCircle(x.toFloat(), y.toFloat(),
                        radius.toFloat(), Path.Direction.CCW)
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                        canvas.clipPath(mPath, Region.Op.DIFFERENCE)
                    else
                        canvas.clipOutPath(mPath)
                    canvas.drawColor(Color.BLACK)

                    if (x > mWinnerRect.left && x < mWinnerRect.right
                        && y > mWinnerRect.top && y < mWinnerRect.bottom) {
                        canvas.drawColor(Color.WHITE);
                        canvas.drawBitmap(mBitmap, mBitmapX.toFloat(),
                            mBitmapY.toFloat(), mPaint);
                        canvas.drawText(
                            "WIN!", mViewWidth.toFloat() / 3,
                            mViewHeight.toFloat() / 2, mPaint);
                    }

                    mPath.rewind()
                    canvas.restore()
                    mSurfaceHolder.unlockCanvasAndPost(canvas)
                } catch (e: Exception) {}
            }
        }
    }

    fun pause() {
        mRunning = false
        try {
            mGameThread.join()
        } catch(e: InterruptedException) {}
    }

    fun resume() {
        mRunning = true
        mGameThread = Thread(this)
        mGameThread.start()
    }

    private fun setUpBitmap() {
        mBitmapX = (Math.floor(Math.random() * (mViewWidth - mBitmap.width))).toInt()
        mBitmapY = (Math.floor(Math.random() * (mViewHeight - mBitmap.height))).toInt()
        mWinnerRect = RectF(mBitmapX.toFloat(), mBitmapY.toFloat(),
            (mBitmapX + mBitmap.width).toFloat(), (mBitmapY + mBitmap.height).toFloat())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        mFlashlightCone = FlashlightCone(mViewWidth, mViewHeight)
        mPaint.textSize = mViewHeight.toFloat() / 5
        mBitmap = BitmapFactory.decodeResource(mContext.resources, R.drawable.android)
        setUpBitmap()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x: Float = event!!.x
        val y: Float = event!!.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                setUpBitmap()
                updateFrame(x.toInt(), y.toInt())
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                updateFrame(x.toInt(), y.toInt())
                invalidate()
            }
            else -> {}
        }
        return true
    }

    private fun updateFrame(newX: Int, newY: Int) {
        mFlashlightCone.update(newX, newY)
    }

}