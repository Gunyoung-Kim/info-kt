package com.gunyoung.infokt.view.controller

import com.gunyoung.infokt.common.service.EmailService
import com.gunyoung.infokt.common.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller

@Controller
class UserViewController(
    val userService: UserService,
    val passwordEncoder: PasswordEncoder,
    val emailService: EmailService
) {
}