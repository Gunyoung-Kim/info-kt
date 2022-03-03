package com.gunyoung.infokt.view.controller

import com.gunyoung.infokt.common.service.EmailService
import com.gunyoung.infokt.common.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class UserViewController(
    val userService: UserService,
    val emailService: EmailService
) {

    companion object {
        const val INDEX_VIEW_PAGE_SIZE = 10
    }

    @GetMapping("/login")
    fun loginView(mav: ModelAndView) : ModelAndView = mav.apply {
        viewName = "login"
    }

    @GetMapping("/join")
    fun joinView(mav: ModelAndView) : ModelAndView = mav.apply {
        viewName = "join"
    }


}