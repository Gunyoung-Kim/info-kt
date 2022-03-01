package com.gunyoung.infokt.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class InfoktApiApplication

fun main(args: Array<String>) {
    SpringApplication.run(InfoktApiApplication::class.java)
}
