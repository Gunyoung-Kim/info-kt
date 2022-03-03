package com.gunyoung.infokt.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class EmailConfig {
    @Bean
    fun javaMailSender() : JavaMailSender = JavaMailSenderImpl()
}