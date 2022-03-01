package com.gunyoung.infokt.common.util

import com.gunyoung.infokt.common.model.ContentEntity
import com.gunyoung.infokt.common.model.RoleType
import com.gunyoung.infokt.common.model.SpaceEntity
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
