package com.gunyoung.infokt.api.controller

import com.gunyoung.infokt.common.code.UserErrorCode
import com.gunyoung.infokt.common.model.ContentDto
import com.gunyoung.infokt.common.model.ContentEntity
import com.gunyoung.infokt.common.model.NotMyResourceException
import com.gunyoung.infokt.common.service.ContentService
import com.gunyoung.infokt.common.service.LinkService
import com.gunyoung.infokt.common.service.SpaceService
import com.gunyoung.infokt.common.service.UserService
import com.gunyoung.infokt.common.util.getSessionUserEmail
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class ContentRestController(
    val contentService: ContentService,
    val spaceService: SpaceService,
    val userService: UserService,
    val linkService: LinkService
) {

    /**
     * userId == contentDto.hostId 는 AOP 로 처리하자
     */
    @PreAuthorize("@userSecurityService.checkIsMineById(#userId)")
    @PostMapping("/contents/{userId}")
    fun createContent(
        @PathVariable userId: Long,
        @Valid @ModelAttribute contentDto: ContentDto
    ) : Unit {
        // todo
    }


    /**
     * isSessionUsers PreAuthorize 로 옮길까 고민
     */
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