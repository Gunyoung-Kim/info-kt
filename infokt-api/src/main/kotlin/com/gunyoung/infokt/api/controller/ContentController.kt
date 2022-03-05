package com.gunyoung.infokt.api.controller

import com.gunyoung.infokt.common.code.UserErrorCode
import com.gunyoung.infokt.common.model.ContentDto
import com.gunyoung.infokt.common.model.ContentEntity
import com.gunyoung.infokt.common.model.NotMyResourceException
import com.gunyoung.infokt.common.service.ContentService
import com.gunyoung.infokt.common.service.LinkService
import com.gunyoung.infokt.common.service.UserService
import com.gunyoung.infokt.common.util.getSessionUserEmail
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class ContentRestController(
    val contentService: ContentService,
    val userService: UserService,
    val linkService: LinkService
) {

    @PostMapping("/contents/{userId}")
    fun createContent(
        @PathVariable userId: Long,
        @Valid @ModelAttribute contentDto: ContentDto
    ) {

    }

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