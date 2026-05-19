package com.voxly.androidapp

import android.app.Application
import com.voxly.shared.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class VoxlyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@VoxlyApp)
            modules(appModules)
        }
    }
}
