package dev.wiskiw.recordmanagerapp.app.logger

import android.util.Log

class AndroidLogger : AppLogger {
    companion object {
        private const val COMMON_TAG = "SpaceApp"
    }

    override fun logError(message: String, exception: Exception?) {
        Log.e(COMMON_TAG, message, exception)
    }
}
