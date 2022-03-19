package com.gunyoung.infokt.api.util

import com.gunyoung.infokt.common.model.SimpleUserInfoDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc

@AutoConfigureMockMvc
abstract class ControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc
}

fun createSampleSimpleUserInfo(): SimpleUserInfoDto = createSampleSimpleUserInfo(1L)

fun createSampleSimpleUserInfo(userId: Long): SimpleUserInfoDto =
    SimpleUserInfoDto(
        userId = userId,
        userName = "test",
        userEmail = "test@test.com"
    )