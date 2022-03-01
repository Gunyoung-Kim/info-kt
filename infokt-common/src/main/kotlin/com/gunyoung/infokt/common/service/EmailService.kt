package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.model.EmailDto
import org.slf4j.Logger
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

interface EmailService {
    fun sendEmail(email: EmailDto)
}

@Service
class EmailServiceImpl(
    val mailSender: JavaMailSender,
    val log: Logger
) : EmailService {

    companion object {
        const val CHAR_SET_FOR_MESSAGE = "utf-8"
    }

    @Async
    override fun sendEmail(email: EmailDto) {

    }
}