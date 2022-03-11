package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.model.EmailDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import org.springframework.mail.javamail.JavaMailSender
import javax.mail.MessagingException
import javax.mail.internet.MimeMessage

@ExtendWith(MockitoExtension::class)
class EmailServiceImplUnitTest {

    @Mock
    lateinit var mailSender: JavaMailSender

    @Mock
    lateinit var log: Logger

    @InjectMocks
    lateinit var emailService: EmailServiceImpl

    lateinit var emailDto: EmailDto

    @BeforeEach
    fun setUp() {
        emailDto = EmailDto(
            senderMail = "sender@test.com",
            senderName = "INFO",
            receiveMail = "receiveMail",
            subject = "subject",
            message = "message"
        )
    }

    @Test
    fun `EmailDto 에 담긴 정보를 통해 Email 을 전송할 때 MimeMessage 생성 과정에서 예외가 발생한다`() {
        // given
        val mimeMessage = mock(MimeMessage::class.java)
        doThrow(MessagingException::class.java).`when`(mimeMessage)
            .setSubject(emailDto.subject, EmailServiceImpl.CHAR_SET_FOR_MESSAGE)
        given(mailSender.createMimeMessage()).willReturn(mimeMessage)

        // when
        emailService.sendEmail(emailDto)

        // then
        then(log).should(times(1)).debug(anyString(), any(MessagingException::class.java))
    }

    @Test
    fun `EmailDto 에 담긴 정보를 통해 Email 을 전송한다`() {
        // given
        val mimeMessage = mock(MimeMessage::class.java)
        given(mailSender.createMimeMessage()).willReturn(mimeMessage)

        // when
        emailService.sendEmail(emailDto)

        // then

        //Then
        then(mailSender).should(times(1)).send(any(MimeMessage::class.java))

        //Then
        then(log).should(times(1))
            .info("Email Send for ${emailDto.subject} to ${emailDto.receiveMail}")
    }
}