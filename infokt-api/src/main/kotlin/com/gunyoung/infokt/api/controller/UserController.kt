package com.gunyoung.infokt.api.controller

import com.gunyoung.infokt.common.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserRestController(
    val userService: UserService
) {

    @GetMapping("/users/email/verification")
    fun emailVerification(
        @RequestParam("email") email: String
    ): String = userService.existsByEmail(email).toString()

    /**
     * todo: PreAuthroize 로 나인지 확인
     */
    @DeleteMapping("/users")
    fun deleteUser(
        @RequestParam("email") targetUserEmail: String
    ) = userService.deleteByEmail(targetUserEmail)
}