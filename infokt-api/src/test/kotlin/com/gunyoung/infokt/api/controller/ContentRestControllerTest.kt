package com.gunyoung.infokt.api.controller

import com.gunyoung.infokt.api.util.ControllerTest
import com.gunyoung.infokt.common.service.ContentService
import com.gunyoung.infokt.common.service.SpaceService
import com.gunyoung.infokt.common.service.UserSecurityService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.then
import org.mockito.kotlin.times
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap

@SpringBootTest
class ContentRestControllerTest: ControllerTest() {

    @MockBean
    lateinit var contentService: ContentService

    @MockBean
    lateinit var spaceService: SpaceService

    @MockBean
    lateinit var userSecurityService: UserSecurityService

    companion object {
        const val CREATE_CONTENT_URL = "/contents"
    }

    @Test
    fun `Content 를 추가할 때 세션 유저의 ID 와 UserId 일치하지 않으면 403 을 던진다`() {
        // given
        val userId = 1L
        given(userSecurityService.checkIsMineById(userId)).willReturn(false)

        // when
        mockMvc.perform(
            post("$CREATE_CONTENT_URL/$userId")
                .params(createSampleContentDtoParams(userId))
        )
        // then
            .andExpect(status().isForbidden)
    }

    @Test
    fun `Content 를 추가할 때 hostId 와 userId 일치하지 않으면 403 을 던진다`() {
        // given
        val userId = 1L
        given(userSecurityService.checkIsMineById(userId)).willReturn(true)

        val hostId = userId + 199
        val params = createSampleContentDtoParams(hostId)

        // when
        mockMvc.perform(
            post("$CREATE_CONTENT_URL/$userId")
                .params(params)
        )
        // then
            .andExpect(status().isForbidden)
    }

    @Test
    fun `Content 를 추가할때 title 이 비어있다면 400 을 던진다`() {
        // given
        val userId = 1L
        given(userSecurityService.checkIsMineById(userId)).willReturn(true)

        val emptyTitle = ""
        val params = createSampleContentDtoParams(userId, emptyTitle)

        // when
        mockMvc.perform(
            post("$CREATE_CONTENT_URL/$userId")
                .params(params)
        )
        // then
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Content 를 추가한다`() {
        // given
        val userId = 1L
        given(userSecurityService.checkIsMineById(userId)).willReturn(true)

        val params = createSampleContentDtoParams(userId)

        // when
        mockMvc.perform(
            post("$CREATE_CONTENT_URL/$userId")
                .params(params)
        )
        // then
            .andExpect(status().isOk)
        then(spaceService).should(times(1)).addContentByUserId(any(), any())
    }

    private fun createSampleContentDtoParams(hostId: Long, title: String) =
        LinkedMultiValueMap<String, String>().apply {
            add("hostId", hostId.toString())
            add("title", title)
            add("description", "description")
            add("contributors", "gallix.kim")
            add("skillStacks", "kotlin")
            add("startedAt", "1999-01-16")
            add("endAt", "1999-01-16")
            add("contents", "contents")
        }

    private fun createSampleContentDtoParams(hostId: Long) =
        createSampleContentDtoParams(hostId, "title")
}