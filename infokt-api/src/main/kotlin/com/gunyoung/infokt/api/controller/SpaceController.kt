package com.gunyoung.infokt.api.controller

import com.gunyoung.infokt.common.service.UserService
import org.springframework.web.bind.annotation.RestController

@RestController
class SpaceRestController(
    val userService: UserService
) {

}