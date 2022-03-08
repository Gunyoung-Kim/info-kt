package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.model.ContentEntity
import com.gunyoung.infokt.common.model.ContentNotFoundException
import com.gunyoung.infokt.common.repository.ContentRepository
import com.gunyoung.infokt.common.util.createSampleContentEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ContentServiceImplUnitTest {

    @Mock
    lateinit var contentRepository: ContentRepository
    @Mock
    lateinit var linkService: LinkService
    @InjectMocks
    lateinit var contentService: ContentServiceImpl

    lateinit var content: ContentEntity

    @BeforeEach
    fun setUp() {
        content = createSampleContentEntity()
        content.id = 1L
    }

    @Test
    fun `Content 를 ID 로 찾을 때 존재하지 않는다면 ContentNotFoundException 을 던진다`() {
        // given
        val nonExistId = 2L

        given(contentRepository.findById(nonExistId)).willReturn(Optional.empty())

        // when, then
        assertThrows<ContentNotFoundException> {
            contentService.findById(nonExistId)
        }
    }

    @Test
    fun `Content 를 ID 로 찾는다`() {
        // given
        val contentId = content.id!!

        given(contentRepository.findById(contentId)).willReturn(Optional.of(content))

        // when
        val result = contentService.findById(contentId)

        // then
        assertEquals(content, result)
    }

    @Test
    fun `Content 를 ID 로 Space 와 User 를 페치조인하여 찾을 떄 존재하지 않는다면 ContentNotFoundException 을 던진다`() {
        // given
        val nonExistId = 2L

        given(contentRepository.findByIdWithSpaceAndUser(nonExistId)).willReturn(null)

        // when, then
        assertThrows<ContentNotFoundException> {
            contentService.findByIdWithSpaceAndUser(nonExistId)
        }
    }

    @Test
    fun `Content 를 ID 로 Space 와 User 를 페치조인하여 찾는다`() {
        // given
        val contentId = content.id!!

        given(contentRepository.findByIdWithSpaceAndUser(contentId)).willReturn(content)

        // when
        val result = contentService.findByIdWithSpaceAndUser(contentId)

        // then
        assertEquals(content, result)
    }

    @Test
    fun `Content 를 ID 로 Links 를 페치조인하여 찾을 때 존재하지 않는다면 ContentNotFoundException 을 던진다`() {
        // given
        val nonExistId = 2L

        given(contentRepository.findByIdWithLinks(nonExistId)).willReturn(null)

        // when, then
        assertThrows<ContentNotFoundException> {
            contentService.findByIdWithLinks(nonExistId)
        }
    }

    @Test
    fun `Content 를 ID 로 Links 를 페치조인하여 찾는다`() {
        // given
        val contentId = content.id!!

        given(contentRepository.findByIdWithLinks(contentId)).willReturn(content)

        // when
        val result = contentService.findByIdWithLinks(contentId)

        // then
        assertEquals(content, result)
    }

    @Test
    fun `Space ID 로 Content 들을 Links 를 페치조인하여 찾는다`() {
        // given
        val spaceId = 1L
        val contents = listOf(content)

        given(contentRepository.findAllBySpaceIdWithLinks(spaceId)).willReturn(contents)

        // when
        val result = contentService.findAllBySpaceIdWithLinks(spaceId)

        // then
        assertEquals(contents, result)
    }

    @Test
    fun `Content 를 삭제할 때 Content 의 ID 가 null 이라면 관련 Link 들을 삭제하는 쿼리가 발생하지 않는다`() {
        // given
        content.id = null

        // when
        contentService.delete(content)

        // then
        then(linkService).should(never()).deleteAllByContentId(any(Long::class.java))
    }

    @Test
    fun `Content 를 삭제한다`() {
        // given
        val contentId = content.id!!

        // when
        contentService.delete(content)

        // then
        then(linkService).should(times(1)).deleteAllByContentId(contentId)
        then(contentRepository).should(times(1)).delete(content)
    }

    @Test
    fun `Content 를 ID 로 찾아 삭제할 때 존재하지 않는다면 ContentNotFoundException 을 던진다`() {
        // given
        val nonExistId = 2L

        given(contentRepository.findById(nonExistId)).willReturn(Optional.empty())

        // when, then
        assertThrows<ContentNotFoundException> {
            contentService.deleteById(nonExistId)
        }
    }

    @Test
    fun `Content 를 ID 로 찾아 삭제한다`() {
        // given
        val contentId = content.id!!

        given(contentRepository.findById(contentId)).willReturn(Optional.of(content))

        // when
        contentService.deleteById(contentId)

        // then
        then(linkService).should(times(1)).deleteAllByContentId(contentId)
        then(contentRepository).should(times(1)).delete(content)
    }

    @Test
    fun `Space ID 로 Content 들을 찾아 삭제한다`() {
        // given
        val spaceId = 1L
        val secondContent = createSampleContentEntity().apply {
            id = 2L
        }
        val contents = listOf(content, secondContent)
        val contentIds = contents.map { it.id!! }

        given(contentRepository.findAllBySpaceIdInQuery(spaceId)).willReturn(contents)

        // when
        contentService.deleteAllBySpaceId(spaceId)

        // then
        verifyLinkServiceForDeleteAllBySpaceId(contentIds)
        then(contentRepository).should(times(1)).deleteAllBySpaceIdInQuery(spaceId)
    }

    private fun verifyLinkServiceForDeleteAllBySpaceId(contentsIds: List<Long>) {
        contentsIds.forEach {
            then(linkService).should(times(1)).deleteAllByContentId(it)
        }
    }

    @Test
    fun `모든 Content 의 개수를 반환한다`() {
        // given
        val contentNum = 1L

        given(contentRepository.count()).willReturn(contentNum)

        // when
        val result = contentService.countAll()

        // then
        assertEquals(contentNum, result)
    }

    @Test
    fun `ID 로 Content 존재여부를 반환할때 존재하지 않는다`() {
        // given
        val nonExistId = 2L
        val isExist = false

        given(contentRepository.existsById(nonExistId)).willReturn(isExist)

        // when
        val result = contentService.existsById(nonExistId)

        // then
        assertFalse(result)
    }

    @Test
    fun `ID 로 Content 존재여부를 반환할때 존재한다`() {
        // given
        val contentId = content.id!!
        val isExist = true

        given(contentRepository.existsById(contentId)).willReturn(isExist)

        // when
        val result = contentService.existsById(contentId)

        // then
        assertTrue(result)
    }
}