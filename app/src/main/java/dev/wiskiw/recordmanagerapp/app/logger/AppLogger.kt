package dev.wiskiw.recordmanagerapp.app.logger

interface AppLogger {
    fun logError(
        message: String,
        exception: Throwable? = null,
    )
}
