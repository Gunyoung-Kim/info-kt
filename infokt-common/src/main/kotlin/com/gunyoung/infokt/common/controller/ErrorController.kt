package com.gunyoung.infokt.common.controller

import com.gunyoung.infokt.common.code.ContentErrorCode
import com.gunyoung.infokt.common.code.UserErrorCode
import com.gunyoung.infokt.common.model.ContentNotFoundException
import com.gunyoung.infokt.common.model.ErrorMsg
import com.gunyoung.infokt.common.model.NotMyResourceException
import com.gunyoung.infokt.common.model.UserNotFoundException
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
    @ExceptionHandler(ContentNotFoundException::class)
    fun handleContentNotFoundException(e: ContentNotFoundException) =
        ErrorMsg(
            ContentErrorCode.CONTENT_NOT_FOUNDED_ERROR.code,
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