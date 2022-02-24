package com.gunyoung.infokt.api.controller

import com.gunyoung.infokt.common.service.ContentService
import com.gunyoung.infokt.common.service.LinkService
import com.gunyoung.infokt.common.service.UserService
import org.springframework.web.bind.annotation.RestController

@RestController
class ContentRestController(
    val contentService: ContentService,
    val userService: UserService,
    val linkService: LinkService
) {

}