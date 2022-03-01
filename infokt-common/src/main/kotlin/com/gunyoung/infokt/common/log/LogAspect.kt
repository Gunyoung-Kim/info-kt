package com.gunyoung.infokt.common.log

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger

@Aspect
class LogAspect(
    val log: Logger
) {
    @Pointcut("within(com.gunyoung.infokt.api.controller..*)")
    private fun controllerLogPointcut() {
    }

//    @Around("controllerLogPointcut()")
//    fun loggingAroundController(joinPoint: ProceedingJoinPoint): Any {
//        val request =  RequestContextHolder.currentRequestAttributes()
//        val params = getStringOfParameters(request)
//    }
//
//    private fun getStringOfParameters(request: HttpServletRequest): Any {
//
//    }
}