package com.example.appwidgetsample

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var mGameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        mGameView = GameView(this)
        mGameView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(mGameView)
    }

    override fun onPause() {
        super.onPause()
        mGameView.pause()
    }

    override fun onResume() {
        super.onResume()
        mGameView.resume()
    }
}