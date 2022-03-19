package com.gunyoung.infokt.api.controller


import com.gunyoung.infokt.api.util.ControllerTest
import com.gunyoung.infokt.api.util.createSampleSimpleUserInfo
import com.gunyoung.infokt.common.service.EmailService
import com.gunyoung.infokt.common.service.UserSecurityService
import com.gunyoung.infokt.common.service.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap

@SpringBootTest
class UserRestControllerUnitTest: ControllerTest() {

    companion object {
        const val ALL_USERS_URL = "/users"
        const val EMAIL_VERIFICATION_URL = "/users/email/verification"
        const val JOIN_URL = "/users"
        const val DELETE_USER_URL = "/users"
    }

    @MockBean
    lateinit var userService: UserService

    @MockBean
    lateinit var emailService: EmailService

    @MockBean
    lateinit var userSecurityService: UserSecurityService

    @Test
    fun `모든 User 리스트를 반환한다`() {
        // given
        val allSimpleUserInfoDto = listOf(
            createSampleSimpleUserInfo(1L),
            createSampleSimpleUserInfo(2L),
            createSampleSimpleUserInfo(3L)
        )
        given(userService.findAllSimpleUserInfo()).willReturn(allSimpleUserInfoDto)

        // when
        val result = mockMvc.perform(get(ALL_USERS_URL)).andReturn()

        // then
        println(result.response.contentAsString)
    }

    @Test
    fun `Email 중복여부를 확인할 때 중복이 확인된다`() {
        // given
        val existEmail = "test@test.com"
        given(userService.existsByEmail(existEmail)).willReturn(true)

        // when
        val result = mockMvc.perform(
            get(EMAIL_VERIFICATION_URL)
                .param("email", existEmail)
        ).andReturn()

        // then
        assertEquals("true", result.response.contentAsString)
    }

    @Test
    fun `회원가입을 신청을 처리할 때 이메일이 비어있다면 400 반환`() {
        // given
        val requestEmail = ""
        given(userService.existsByEmail(requestEmail)).willReturn(false)

        val requestParams = createUserJoinDtoParam(requestEmail)

        // when, then
        mockMvc.perform(
            post(JOIN_URL)
                .params(requestParams)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `회원가입 신청을 처리할 때 이메일이 이미 존재한다면 409 반환`() {
        // given
        val requestEmail = "test@test.com"
        given(userService.existsByEmail(requestEmail)).willReturn(true)

        val requestParams = createUserJoinDtoParam(requestEmail)

        // when, then
        mockMvc.perform(
            post(JOIN_URL)
                .params(requestParams)
        ).andExpect(status().isConflict)
    }

    @Test
    fun `회원가입 신청을 처리 한 후 환영 이메일을 전송한다`() {
        // given
        val requestEmail = "newby@test.com"
        given(userService.existsByEmail(requestEmail)).willReturn(false)

        val requestParams = createUserJoinDtoParam(requestEmail)

        // when
        mockMvc.perform(
            post(JOIN_URL)
                .params(requestParams)
        ).andExpect(status().isOk)

        // then
        then(emailService).should(times(1)).sendEmail(any())
    }

    private fun createUserJoinDtoParam(email: String) = LinkedMultiValueMap<String, String>().apply {
        add("email", email)
        add("password", "abcd1234!")
        add("firstName", "테스")
        add("lastName", "트")
    }

    @Test
    fun `User 삭제 요청이 들어왔을 대 해당 계정이 내것이 아니면 403 반환`() {
        // given
        val anotherEmail = "another@test.com"
        given(userSecurityService.checkIsMineByEmail(anotherEmail)).willReturn(false)

        // when, then
        mockMvc.perform(
            delete(DELETE_USER_URL)
                .param("email", anotherEmail)
        ).andExpect(status().isForbidden)
    }

    @Test
    fun `User 삭제 요청이 들어왔을 때 해당 계정이 존재하지 않으면 403 반환`() {
        // given
        val nonExistEmail = "nonExist@test.com"
        given(userSecurityService.checkIsMineByEmail(nonExistEmail)).willReturn(false)

        // when, then
        mockMvc.perform(
            delete(DELETE_USER_URL)
                .param("email", nonExistEmail)
        ).andExpect(status().isForbidden)
    }

    @Test
    fun `User 삭제 요청을 처리한다`() {
        // given
        val email = "test@test.com"
        given(userSecurityService.checkIsMineByEmail(email)).willReturn(true)

        // when
        mockMvc.perform(
            delete(DELETE_USER_URL)
                .param("email", email)
        )
            // then
            .andExpect(status().isOk)

        then(userService).should(times(1)).deleteByEmail(email)
    }

}