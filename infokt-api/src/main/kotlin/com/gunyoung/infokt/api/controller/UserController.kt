package com.gunyoung.infokt.api.controller

import com.gunyoung.infokt.common.code.UserErrorCode
import com.gunyoung.infokt.common.model.SimpleUserInfoDto
import com.gunyoung.infokt.common.model.UserEmailDuplicationException
import com.gunyoung.infokt.common.model.UserJoinDto
import com.gunyoung.infokt.common.service.UserService
import com.gunyoung.infokt.common.util.notReturn
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class UserRestController(
    val userService: UserService
) {

    @GetMapping("/users")
    fun allUsers() : List<SimpleUserInfoDto> = userService.findAllSimpleUserInfo()

    @GetMapping("/users/email/verification")
    fun emailVerification(
        @RequestParam("email") email: String
    ): String = userService.existsByEmail(email).toString()

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

@Component
@Aspect
class UserRestControllerAspect(
    val userService: UserService
) {

    @Pointcut("execution(* com.gunyoung.infokt.api.controller.UserRestController.join(..)")
    private fun userRestControllerJoinMethod() {
    }

    @Before("userRestControllerJoinMethod() && args(userJoinDto,..)")
    fun checkDuplicationOfUserEmail(userJoinDto: UserJoinDto) {
        if(userService.existsByEmail(userJoinDto.email)) {
            throw UserEmailDuplicationException(UserErrorCode.USER_DUPLICATION_FOUNDED_ERROR.description)
        }
    }
}