package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.model.ContentEntity
import com.gunyoung.infokt.common.model.LinkEntity
import com.gunyoung.infokt.common.model.LinkMapper
import com.gunyoung.infokt.common.model.LinkNotFoundException
import com.gunyoung.infokt.common.repository.LinkRepository
import com.gunyoung.infokt.common.util.createSampleContentEntity
import com.gunyoung.infokt.common.util.createSampleLinkEntity
import com.gunyoung.infokt.common.util.createSampleLinkUpdateDto
import org.junit.jupiter.api.Assertions.assertEquals
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
class LinkServiceImplUnitTest(
    @Mock
    val linkRepository: LinkRepository,
    @Mock
    val linkMapper: LinkMapper,
    @InjectMocks
    val linkService: LinkServiceImpl
) {

    lateinit var link: LinkEntity

    @BeforeEach
    private fun setUp() {
        link = createSampleLinkEntity()
        link.id = 1L
    }

    @Test
    fun `Link 를 ID 로 찾을 때 존재하지 않는다면 LinkNotFoundException 을 던진다`() {
        // given
        val nonExistId = 2L

        given(linkRepository.findById(nonExistId)).willReturn(Optional.empty())

        // when, then
        assertThrows<LinkNotFoundException> {
            linkService.findById(nonExistId)
        }
    }

    @Test
    fun `Link 를 ID 로 찾는다`() {
        // given
        val linkId = link.id!!

        given(linkRepository.findById(linkId)).willReturn(Optional.of(link))

        // when
        val result = linkService.findById(linkId)

        // then
        assertEquals(link, result)
    }

    @Test
    fun `Content ID 로 Link 들을 찾는다`() {
        // given
        val contentId = 1L
        val links = listOf(link)

        given(linkRepository.findAllByContentId(contentId)).willReturn(links)

        // when
        val result = linkService.findAllByContentId(contentId)

        // then
        assertEquals(links, result)
    }

    @Test
    fun `Link 들을 저장한다`() {
        // given
        val links = listOf(link)

        given(linkRepository.saveAll(links)).willReturn(links)

        // when
        val result = linkService.saveAll(links)

        // then
        assertEquals(links, result)
    }

    @Test
    fun `Content 의 Link 들을 업데이트할 때 존재하지 않았던 Link 는 추가한다`() {
        // given
        val content = createSampleContentEntity().clearLinks()
        val linkUpdateDto = createSampleLinkUpdateDto()

        // when
        linkService.updateLinksForContent(content, listOf(linkUpdateDto))

        // then
        then(linkMapper).should(times(1)).linkUpdateDtoToEntity(linkUpdateDto)
    }

    @Test
    fun `Content 의 Link 들을 업데이트할 때 존재하는 Link 들을 수정한다`() {
        // given
        val content = createSampleContentEntity().clearLinks()
        content.linkEntities = listOf(link)
        val linkUpdateDto = createSampleLinkUpdateDto(link.id!!)

        // when
        linkService.updateLinksForContent(content, listOf(linkUpdateDto))

        // then
        then(linkMapper).should(times(1)).updateEntityFromLinkUpdateDto(link, linkUpdateDto)
    }

    @Test
    fun `Content 의 Link 들을 업데이트할 때 삭제해야할 Link 들은 삭제한다`() {
        // given
        val content = createSampleContentEntity().clearLinks()
        content.linkEntities = listOf(link)

        // when
        linkService.updateLinksForContent(content, listOf())

        // then
        then(linkRepository).should(times(1)).delete(link)
    }

    private fun ContentEntity.clearLinks() = apply {
        linkEntities = mutableListOf()
    }

    @Test
    fun `Link 를 삭제한다`() {
        // given

        // when
        linkService.delete(link)

        // then
        then(linkRepository).should(times(1)).delete(link)
    }

    @Test
    fun `Link 를 ID 로 찾고 삭제할 때 존재하지 않으면 LinkNotFoundException 을 던진다`() {
        // given
        val nonExistId = 2L

        given(linkRepository.findById(nonExistId)).willReturn(Optional.empty())

        // when, then
        assertThrows<LinkNotFoundException> {
            linkService.findById(nonExistId)
        }
    }

    @Test
    fun `Link 를 ID 로 찾고 삭제한다`() {
        // given
        val linkId = link.id!!

        given(linkRepository.findById(linkId)).willReturn(Optional.of(link))

        // when
        linkService.deleteById(linkId)

        // then
        then(linkRepository).should(times(1)).delete(link)
    }

    @Test
    fun `Link 들을 Content ID 로 찾아서 삭제한다`() {
        // given
        val contentId = 1L

        // when
        linkService.deleteAllByContentId(contentId)

        // then
        then(linkRepository).should(times(1)).deleteAllByContentIdInQuery(contentId)
    }
}