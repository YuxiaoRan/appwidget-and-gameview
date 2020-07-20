package com.example.appwidgetsample

class FlashlightCone {
    var mX: Int
    var mY: Int
    var mRadius: Int

    constructor(viewWidth: Int, viewHeight: Int) {
        mX = viewWidth / 2
        mY = viewHeight / 2
        mRadius = if (viewWidth <= viewHeight) (mX / 3) else (mY / 3)
    }

    fun update(newX: Int, newY: Int) {
        mX = newX
        mY = newY
    }
}