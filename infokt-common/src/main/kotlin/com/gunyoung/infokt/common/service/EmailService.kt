package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.model.EmailDto
import org.slf4j.Logger
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import javax.mail.Message
import javax.mail.internet.InternetAddress

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
        try {
            mailSender.createMimeMessage().apply {
                addRecipient(Message.RecipientType.TO, InternetAddress(email.receiveMail))
                addFrom(arrayOf(InternetAddress(email.senderMail, email.senderName)))
                setSubject(email.subject, CHAR_SET_FOR_MESSAGE)
                setText(email.message, CHAR_SET_FOR_MESSAGE)
                mailSender.send(this)
            }
            log.info("Email Send for ${email.subject} to ${email.receiveMail}")
        } catch (e: Exception) {
            log.debug("Exception occurred while sending email", e)
        }
    }
}