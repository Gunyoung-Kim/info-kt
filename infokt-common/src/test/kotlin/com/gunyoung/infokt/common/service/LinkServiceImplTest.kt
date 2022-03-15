package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.TestConfig
import com.gunyoung.infokt.common.model.*
import com.gunyoung.infokt.common.repository.ContentRepository
import com.gunyoung.infokt.common.repository.LinkRepository
import com.gunyoung.infokt.common.util.createSampleContentEntity
import com.gunyoung.infokt.common.util.createSampleLinkEntity
import com.gunyoung.infokt.common.util.createSampleLinkUpdateDto
import com.gunyoung.infokt.common.util.getNonExistIdForLinkEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.util.*

@ExtendWith(MockitoExtension::class)
class LinkServiceImplUnitTest {

    @Mock
    lateinit var linkRepository: LinkRepository
    @Mock
    lateinit var linkMapper: LinkMapper
    @InjectMocks
    lateinit var linkService: LinkServiceImpl

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
        val newLinkEntity = createSampleLinkEntity();

        given(linkMapper.linkUpdateDtoToEntity(linkUpdateDto)).willReturn(newLinkEntity);

        // when
        linkService.updateLinksForContent(content, listOf(linkUpdateDto))

        // then
        assertEquals(content, newLinkEntity.contentEntity)
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

@SpringBootTest
@Import(TestConfig::class)
class LinkServiceImplTest {

    @Autowired
    lateinit var linkRepository: LinkRepository

    @Autowired
    lateinit var linkMapper: LinkMapper

    @Autowired
    lateinit var linkService: LinkServiceImpl

    @Autowired
    lateinit var contentRepository: ContentRepository

    lateinit var link: LinkEntity

    @BeforeEach
    fun setUp() {
        link = createSampleLinkEntity()
        linkRepository.save(link)
    }

    @AfterEach
    fun tearDown() {
        linkRepository.deleteAll()
        contentRepository.deleteAll()
    }

    @Test
    fun `ID 로 Link 를 찾을 때 존재하지 않으면 LinkNotFoundException 을 던진다`() {
        // given
        val nonExistId = getNonExistIdForLinkEntity(linkRepository)

        // when, then
        assertThrows<LinkNotFoundException> {
            linkService.findById(nonExistId)
        }
    }

    @Test
    fun `ID 로 Link 를 찾는다`() {
        // given
        val linkId = link.id!!

        // when
        val result = linkService.findById(linkId)

        // then
        assertEquals(linkId, result.id)
    }

    @Test
    fun `Content 의 링크를 업데이트할 때 기존에 없던 Link 는 추가된다`() {
        // given
        val content = createSampleContentAndSaveIt()
        val linkUpdateDto = createSampleLinkUpdateDto()

        val beforeNumOfLink = linkRepository.count()

        // when
        linkService.updateLinksForContent(contentRepository.findByIdWithLinks(content.id!!)!!, listOf(linkUpdateDto))

        // then
        assertEquals(beforeNumOfLink + 1, linkRepository.count())
    }

    @Test
    fun `Content 의 링크를 업데이트할 때 기존의 Link 를 수정한다`() {
        // given
        val content = createSampleContentAndSaveIt()
        link.setContentForExistLinkEntity(content)
        val linkUpdateDto = createSampleLinkUpdateDto(link.id!!)

        val beforeNumOfLink = linkRepository.count()

        // when
        linkService.updateLinksForContent(contentRepository.findByIdWithLinks(content.id!!)!!, listOf(linkUpdateDto))

        // then
        assertEquals(beforeNumOfLink, linkRepository.count())
        linkRepository.findById(link.id!!).get().verifyWithLink(linkUpdateDto)
    }

    private fun LinkEntity.verifyWithLink(linkUpdateDto: LinkUpdateDto) = let {
        assertEquals(linkUpdateDto.linkTag, tag)
        assertEquals(linkUpdateDto.linkURL, url)
    }

    @Test
    fun `Content 의 링크를 업데이트할 때 삭제할 Link 를 삭제한다`() {
        // given
        val content = createSampleContentAndSaveIt()
        link.setContentForExistLinkEntity(content)

        val beforeNumOfLink = linkRepository.count()

        // when
        linkService.updateLinksForContent(contentRepository.findByIdWithLinks(content.id!!)!!, listOf())

        // then
        assertEquals(beforeNumOfLink - 1, linkRepository.count())
    }

    private fun createSampleContentAndSaveIt(): ContentEntity = contentRepository.save(createSampleContentEntity())

    private fun LinkEntity.setContentForExistLinkEntity(content: ContentEntity) = apply {
        contentEntity = content
        linkRepository.save(this)
    }

    @Test
    fun `Link 를 삭제한다`() {
        // given
        val linkId = link.id!!

        // when
        linkService.delete(link)

        // then
        assertFalse(linkRepository.existsById(linkId))
    }

    @Test
    fun `Link 를 ID 로 삭제할 때 존재하지 않으면 LinkNotFoundException 을 던진다`() {
        // given
        val nonExistId = getNonExistIdForLinkEntity(linkRepository)

        // when, then
        assertThrows<LinkNotFoundException> {
            linkService.deleteById(nonExistId)
        }
    }

    @Test
    fun `Link 를 ID 로 삭제한다`() {
        // given
        val linkId = link.id!!

        // when
        linkService.deleteById(linkId)

        // then
        assertFalse(linkRepository.existsById(linkId))
    }
}