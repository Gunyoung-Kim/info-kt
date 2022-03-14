package com.gunyoung.infokt.common.util

import com.gunyoung.infokt.common.model.*
import com.gunyoung.infokt.common.repository.ContentRepository
import com.gunyoung.infokt.common.repository.SpaceRepository
import com.gunyoung.infokt.common.repository.UserRepository
import org.mockito.Mockito

const val DEFAULT_USER_EMAIL = "test@test.com"

fun createSampleUserEntity(): UserEntity = createSampleUserEntity(DEFAULT_USER_EMAIL, RoleType.USER)

fun createSampleUserEntity(email: String) = createSampleUserEntity(email, RoleType.USER)

fun createSampleUserEntity(roleType: RoleType) = createSampleUserEntity(DEFAULT_USER_EMAIL, roleType)

fun createSampleUserEntity(email: String, roleType: RoleType): UserEntity =
    UserEntity(
        email = email,
        password = "abcd1234!",
        firstName = "스트",
        lastName = "테",
        role = roleType
    )

fun createSampleUserJoinDto(): UserJoinDto = createSampleUserJoinDto(DEFAULT_USER_EMAIL)

fun createSampleUserJoinDto(email: String): UserJoinDto =
    UserJoinDto(
        email = email,
        password = "abcd1234!",
        firstName = "스트",
        lastName = "테"
    )

fun getNonExistIdForUserEntity(userRepository: UserRepository): Long = userRepository.findAll().maxOf { it.id!! } + 1

fun createSampleSpaceEntity(): SpaceEntity =
    SpaceEntity(
        description = "description"
    )

fun getNonExistIdForSpaceEntity(spaceRepository: SpaceRepository): Long = spaceRepository.findAll().maxOf { it.id!! } + 1

const val DEFAULT_CONTENT_TITLE = "title"

fun createSampleContentEntity(): ContentEntity = createSampleContentEntity(DEFAULT_CONTENT_TITLE)

fun createSampleContentEntity(title: String): ContentEntity =
    ContentEntity(
        title = title,
        description = "description",
        contributors = "contributors",
        contents = "contents"
    )

fun getNonExistIdForContentEntity(contentRepository: ContentRepository): Long = contentRepository.findAll().maxOf { it.id!! } + 1

fun createSampleLinkEntity(): LinkEntity =
    LinkEntity(
        tag = "tag",
        url = "http://localhost"
    )

fun createSampleLinkUpdateDto(): LinkUpdateDto =
    LinkUpdateDto(
        linkTag = "updateLink",
        linkURL = "https://update.com"
    )

fun createSampleLinkUpdateDto(linkId: Long): LinkUpdateDto =
    LinkUpdateDto(
        linkId = linkId,
        linkTag = "updateLink",
        linkURL = "https://update.com"
    )

inline fun <reified T> any(type: Class<T>): T = Mockito.any(type)