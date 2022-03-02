package com.gunyoung.infokt.api.controller

import com.gunyoung.infokt.common.model.UserJoinDto
import com.gunyoung.infokt.common.service.UserService
import com.gunyoung.infokt.common.util.notReturn
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class UserRestController(
    val userService: UserService
) {

    @GetMapping("/users/email/verification")
    fun emailVerification(
        @RequestParam("email") email: String
    ): String = userService.existsByEmail(email).toString()

    /**
     * todo :
     * 이메일 중복확인
     * -> 서비스레이어에서 할지, 프레젠테이션 레이어에서 할지 결정
     */
    @PostMapping("/users")
    fun join(
        @ModelAttribute("formModel") @Valid userJoinDto: UserJoinDto
    ): Unit = userService.createNewUser(userJoinDto).notReturn()

    /**
     * todo: PreAuthroize 로 나인지 확인
     */
    @DeleteMapping("/users")
    fun deleteUser(
        @RequestParam("email") targetUserEmail: String
    ) = userService.deleteByEmail(targetUserEmail)
}