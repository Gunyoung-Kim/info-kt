package com.gunyoung.infokt.api.controller

import com.gunyoung.infokt.common.code.UserErrorCode
import com.gunyoung.infokt.common.model.ContentDto
import com.gunyoung.infokt.common.model.ContentEntity
import com.gunyoung.infokt.common.model.NotMyResourceException
import com.gunyoung.infokt.common.service.ContentService
import com.gunyoung.infokt.common.service.SpaceService
import com.gunyoung.infokt.common.util.getSessionUserEmail
import com.gunyoung.infokt.common.util.notReturn
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class ContentRestController(
    val contentService: ContentService,
    val spaceService: SpaceService
) {

    @PreAuthorize("@userSecurityService.checkIsMineById(#userId)")
    @PostMapping("/contents/{userId}")
    fun createContent(
        @PathVariable userId: Long,
        @Valid @ModelAttribute contentDto: ContentDto
    ): Unit = spaceService.addContentByUserId(userId, contentDto).notReturn()

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

@Component
@Aspect
class ContentRestControllerAspect(
) {

    @Pointcut("execution(* com.gunyoung.infokt.api.controller.ContentRestController.createContent(..))")
    private fun createContentMethod() {
    }

    @Before("createContentMethod() && args(userId,contentDto,..)")
    fun checkSessionUserEqualsToHost(userId: Long, contentDto: ContentDto) {
        if (userId != contentDto.hostId) {
            throw NotMyResourceException(UserErrorCode.RESOURCE_IS_NOT_MINE_ERROR.description)
        }
    }
}