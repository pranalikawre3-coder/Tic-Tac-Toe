package com.example.tictactoe

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TicTacToeApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}