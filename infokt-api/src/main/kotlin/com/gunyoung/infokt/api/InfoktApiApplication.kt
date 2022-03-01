package com.gunyoung.infokt.api

import com.gunyoung.infokt.common.config.CommonConfigs
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(
    CommonConfigs::class
)
class InfoktApiApplication

fun main(args: Array<String>) {
    SpringApplication.run(InfoktApiApplication::class.java)
}
