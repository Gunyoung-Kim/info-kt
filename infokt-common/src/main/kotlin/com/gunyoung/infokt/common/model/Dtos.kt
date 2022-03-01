package com.gunyoung.infokt.common.model

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class ErrorMsg(
    val errorCode: String,
    val description: String
)

// User

data class UserJoinDto(
    @NotEmpty
    @Size(max = 50)
    val email: String,
    @NotEmpty
    val password: String,
    @NotEmpty
    @Size(max = 60)
    val firstName: String,
    @NotEmpty
    @Size(max = 60)
    val lastName: String
)

data class SimpleUserInfoDto(
    val userId: Long,
    val userName: String,
    val userEmail: String
)

// Link

data class UpdateLinkDto(
    val linkId: Long,
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