package dev.wiskiw.recordmanagerapp.data.model

open class DataLayerException : Exception {
    constructor(s: String?) : super(s)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
