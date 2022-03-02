package com.gunyoung.infokt.api.controller

import com.gunyoung.infokt.common.code.UserErrorCode
import com.gunyoung.infokt.common.model.ContentEntity
import com.gunyoung.infokt.common.model.NotMyResourceException
import com.gunyoung.infokt.common.service.ContentService
import com.gunyoung.infokt.common.service.LinkService
import com.gunyoung.infokt.common.service.UserService
import com.gunyoung.infokt.common.util.getSessionUserEmail
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ContentRestController(
    val contentService: ContentService,
    val userService: UserService,
    val linkService: LinkService
) {

    @DeleteMapping("/contents/{id}")
    fun deleteContent(
        @PathVariable("id") id: Long
    ): Unit = contentService.findByIdWithSpaceAndUser(id)
        .isSessionUsers()
        .let { contentService.delete(it) }

    private fun ContentEntity.isSessionUsers() = apply {
        val sessionUserEmail = getSessionUserEmail()
        if (sessionUserEmail != spaceEntity?.userEntity?.email) {
            throw NotMyResourceException(UserErrorCode.RESOURCE_IS_NOT_MINE_ERROR.description)
        }
    }
}