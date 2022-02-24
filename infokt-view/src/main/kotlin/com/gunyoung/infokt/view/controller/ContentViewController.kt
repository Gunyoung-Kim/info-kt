package com.gunyoung.infokt.view.controller

import com.gunyoung.infokt.common.service.ContentService
import com.gunyoung.infokt.common.service.LinkService
import com.gunyoung.infokt.common.service.UserService
import org.springframework.stereotype.Controller

@Controller
class ContentViewController(
    val userService: UserService,
    val contentService: ContentService,
    val linkService: LinkService
) {
}