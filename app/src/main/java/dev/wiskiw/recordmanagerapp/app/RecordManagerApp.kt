package dev.wiskiw.recordmanagerapp.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RecordManagerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initDi()
    }

    private fun initDi() {
        startKoin {
            androidContext(this@RecordManagerApp)
            allowOverride(false)

            modules(
                appModule,
                viewModelModule,
            )
        }
    }
}
