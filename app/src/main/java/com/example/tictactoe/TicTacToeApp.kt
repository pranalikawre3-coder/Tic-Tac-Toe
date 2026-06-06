package com.example.tictactoe

import android.app.Application
import dragger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TicTacToeApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}