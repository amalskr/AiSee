package org.aisee.app

import android.app.Application
import org.aisee.app.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AiSeeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AiSeeApplication)
            modules(appModule)
        }
    }
}
