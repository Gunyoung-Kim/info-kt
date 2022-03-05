package com.gunyoung.infokt.common.async

import org.slf4j.Logger
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
class AsyncExceptionHandler(
    val log: Logger
) : AsyncUncaughtExceptionHandler {

    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any?) {
        log.error("Uncaught asynchronous exception from: ${method.declaringClass.name}.${method.name}")
    }
}