package com.gunyoung.infokt.common.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LogConfig {

    @Bean
    fun logger() : Logger = LoggerFactory.getLogger(LogAspect::class.java)

    @Bean
    fun logAspect() : LogAspect = LogAspect(logger())
}