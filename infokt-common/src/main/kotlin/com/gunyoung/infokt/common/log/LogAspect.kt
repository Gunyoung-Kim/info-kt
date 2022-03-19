package com.gunyoung.infokt.common.log

import com.gunyoung.infokt.common.util.getRequestIp
import com.gunyoung.infokt.common.util.getStringOfParameters
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
class LogAspect(
    private val log: Logger
) {
    @Pointcut("within(com.gunyoung.infokt.api.controller..*)")
    private fun controllerLogPointcut() {
    }

    @Around("controllerLogPointcut()")
    fun loggingAroundController(joinPoint: ProceedingJoinPoint): Any? {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val params = request.getStringOfParameters()
        val requestIp = request.getRequestIp()

        val beforeProceedTime = System.currentTimeMillis()
        try {
            return joinPoint.proceed(joinPoint.args)
        } finally {
            val afterProceedTime = System.currentTimeMillis()
            log.info("Request: ${request.method} ${request.requestURI}${params} < $requestIp ${afterProceedTime - beforeProceedTime}")
        }
    }

}