package dev.wiskiw.recordmanagerapp.app

import android.app.Application
import androidx.room.Room
import dev.wiskiw.recordmanagerapp.data.room.AppRoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RecordManagerApp : Application() {

    companion object {
        const val ROOM_DB_NAME = "record-manager-database"
    }

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
                dataModule,
                viewModelModule,
            )
        }
    }
}
