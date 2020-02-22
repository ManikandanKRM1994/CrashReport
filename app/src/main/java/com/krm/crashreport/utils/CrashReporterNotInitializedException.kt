package com.krm.crashreport.utils

import com.krm.crashreport.utils.CrashReporterException

class CrashReporterNotInitializedException : CrashReporterException {

    constructor() : super()

    constructor(message: String?) : super(message)

    constructor(message: String?, throwable: Throwable?) : super(message, throwable)

    constructor(throwable: Throwable?) : super(throwable)

    companion object {
        const val serialVersionUID: Long = 1
    }
}