package com.gunyoung.infokt.common.util

import com.gunyoung.infokt.common.model.*

const val DEFAULT_USER_EMAIL = "test@test.com"

fun createSampleUserEntity() : UserEntity = createSampleUserEntity(DEFAULT_USER_EMAIL, RoleType.USER)

fun createSampleUserEntity(email: String) = createSampleUserEntity(email, RoleType.USER)

fun createSampleUserEntity(roleType: RoleType) = createSampleUserEntity(DEFAULT_USER_EMAIL, roleType)

fun createSampleUserEntity(email: String, roleType: RoleType) : UserEntity =
    UserEntity(
        email = email,
        password = "abcd1234!",
        firstName = "스트",
        lastName = "테",
        role = roleType
    )

fun createSampleUserJoinDto() : UserJoinDto = createSampleUserJoinDto(DEFAULT_USER_EMAIL)

fun createSampleUserJoinDto(email: String) : UserJoinDto =
    UserJoinDto(
        email = email,
        password = "abcd1234!",
        firstName = "스트",
        lastName = "테"
    )

fun createSampleSpaceEntity() : SpaceEntity =
    SpaceEntity(
        description = "description"
    )

const val DEFAULT_CONTENT_TITLE = "title"

fun createSampleContentEntity() : ContentEntity = createSampleContentEntity(DEFAULT_CONTENT_TITLE)

fun createSampleContentEntity(title: String) : ContentEntity =
    ContentEntity(
        title = title,
        description = "description",
        contributors = "contributors",
        contents = "contents"
    )
