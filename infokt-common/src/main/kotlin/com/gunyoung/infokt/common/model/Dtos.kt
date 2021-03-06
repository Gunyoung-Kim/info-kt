package com.gunyoung.infokt.common.model

import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class ErrorMsg(
    val errorCode: String,
    val description: String
)

// User

data class UserJoinDto(
    @field:NotEmpty
    @field:Size(max = 50)
    val email: String,
    @field:NotEmpty
    val password: String,
    @field:NotEmpty
    @field:Size(max = 60)
    val firstName: String,
    @field:NotEmpty
    @field:Size(max = 60)
    val lastName: String
)

data class SimpleUserInfoDto(
    val userId: Long,
    val userName: String,
    val userEmail: String
)

// Content

data class ContentDto(
    val hostId: Long,
    @field:NotEmpty
    val title: String,
    val description: String,
    @field:NotEmpty
    val contributors: String,
    val skillStacks: String,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val startedAt: Date,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val endAt: Date,
    val contents: String,
    val links: List<LinkUpdateDto> = emptyList()
)


// Link

data class LinkUpdateDto(
    val linkId: Long? = null,
    val linkTag: String,
    val linkURL: String
)

// Email

data class EmailDto(
    val senderName: String,
    val senderMail: String,
    val receiveMail: String,
    val subject: String,
    val message: String
)