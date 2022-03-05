package com.gunyoung.infokt.common.controller

import com.gunyoung.infokt.common.code.ContentErrorCode
import com.gunyoung.infokt.common.code.LinkErrorCode
import com.gunyoung.infokt.common.code.SpaceErrorCode
import com.gunyoung.infokt.common.code.UserErrorCode
import com.gunyoung.infokt.common.model.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(e: UserNotFoundException) =
        ErrorMsg(
            UserErrorCode.USER_NOT_FOUNDED_ERROR.code,
            e.message!!
        )

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SpaceNotFoundException::class)
    fun handleSpaceNotFoundException(e: SpaceNotFoundException) =
        ErrorMsg(
            SpaceErrorCode.SPACE_NOT_FOUND_ERROR.code,
            e.message!!
        )

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ContentNotFoundException::class)
    fun handleContentNotFoundException(e: ContentNotFoundException) =
        ErrorMsg(
            ContentErrorCode.CONTENT_NOT_FOUNDED_ERROR.code,
            e.message!!
        )

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LinkNotFoundException::class)
    fun handleLinkNotFoundException(e: LinkNotFoundException) =
        ErrorMsg(
            LinkErrorCode.LINK_NOT_FOUND_ERROR.code,
            e.message!!
        )

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserEmailDuplicationException::class)
    fun handleUserEmailDuplicationException(e: UserEmailDuplicationException) =
        ErrorMsg(
            UserErrorCode.USER_DUPLICATION_FOUNDED_ERROR.code,
            e.message!!
        )

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotMyResourceException::class)
    fun handleNotMyResourceException(e: NotMyResourceException) =
        ErrorMsg(
            UserErrorCode.RESOURCE_IS_NOT_MINE_ERROR.code,
            e.message!!
        )
}