package com.gunyoung.infokt.common.util

import com.gunyoung.infokt.common.model.RoleType
import com.gunyoung.infokt.common.model.UserEntity

const val DEFAULT_USER_EMAIL = "test@test.com"

fun createSampleUserEntity() : UserEntity = createSampleUserEntity(DEFAULT_USER_EMAIL, RoleType.USER)

fun createSampleUserEntity(roleType: RoleType) = createSampleUserEntity(DEFAULT_USER_EMAIL, roleType)

fun createSampleUserEntity(email: String, roleType: RoleType) : UserEntity =
    UserEntity(
        email = email,
        password = "abcd1234!",
        firstName = "스트",
        lastName = "테",
        role = roleType
    )
