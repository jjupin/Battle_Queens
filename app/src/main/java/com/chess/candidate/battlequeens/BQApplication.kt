package com.chess.candidate.battlequeens

import android.app.Application
import com.chess.candidate.battlequeens.di.modules.playGameModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.android.ext.koin.androidContext

class BQApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BQApplication)
            modules(playGameModule)
        }
    }
}