package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.TestConfig
import com.gunyoung.infokt.common.model.ContentNumLimitExceedException
import com.gunyoung.infokt.common.model.SpaceEntity
import com.gunyoung.infokt.common.model.SpaceNotFoundException
import com.gunyoung.infokt.common.repository.ContentRepository
import com.gunyoung.infokt.common.repository.SpaceRepository
import com.gunyoung.infokt.common.util.createSampleContentEntity
import com.gunyoung.infokt.common.util.createSampleSpaceEntity
import com.gunyoung.infokt.common.util.getNonExistIdForSpaceEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
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
class SpaceServiceImplUnitTest {

    @Mock
    lateinit var spaceRepository: SpaceRepository
    @Mock
    lateinit var contentService: ContentService
    @InjectMocks
    lateinit var spaceService: SpaceServiceImpl

    lateinit var space: SpaceEntity

    @BeforeEach
    fun setUp() {
        space = createSampleSpaceEntity()
        space.id = 1L
    }

    @Test
    fun `ID로 Space 를 찾을 때 존재하지 않으면 SpaceNotFoundException 을 던진다`() {
        // given
        val nonExistId = 2L

        given(spaceRepository.findById(nonExistId)).willReturn(Optional.empty())

        // when, then
        assertThrows<SpaceNotFoundException> {
            spaceService.findById(nonExistId)
        }
    }

    @Test
    fun `ID로 Space 를 찾을 때 존재하면 SpaceEntity를 반환한다`() {
        // given
        val spaceId = space.id!!

        given(spaceRepository.findById(spaceId)).willReturn(Optional.of(space))

        // when
        val result = spaceService.findById(spaceId)

        // then
        assertEquals(space, result)
    }

    @Test
    fun `모든 Space 를 찾는다`() {
        // given
        val allSpace = listOf(space)

        given(spaceRepository.findAll()).willReturn(allSpace)

        // when
        val result = spaceService.findAll()

        // then
        assertEquals(allSpace, result)
    }

    @Test
    fun `Space 를 삭제할 때 id 가 없으면 아무일도 일어나지 않는다`() {
        // given
        space.id = null

        // when
        spaceService.delete(space)

        // then
        then(contentService).should(never()).deleteAllBySpaceId(any(Long::class.java))
        then(spaceRepository).should(never()).deleteByIdInQuery(any(Long::class.java))
    }

    @Test
    fun `Space 를 삭제한다`() {
        // given
        val spaceId = space.id!!

        // when
        spaceService.delete(space)

        // then
        then(contentService).should(times(1)).deleteAllBySpaceId(spaceId)
        then(spaceRepository).should(times(1)).deleteByIdInQuery(spaceId)
    }

    @Test
    fun `ID로 Space 의 존재여부를 확인하는데 존재하지 않는다`() {
        // given
        val nonExistId = 2L
        val isExist = false

        given(spaceRepository.existsById(nonExistId)).willReturn(isExist)

        // when
        val result = spaceService.existsById(nonExistId)

        // then
        assertFalse(result)
    }

    @Test
    fun `ID로 Space 의 존재여부를 확인하는데 존재한다`() {
        // given
        val spaceId = space.id!!
        val isExist = true

        given(spaceRepository.existsById(spaceId)).willReturn(isExist)

        // when
        val result = spaceService.existsById(spaceId)

        // then
        assertTrue(result)
    }

    @Test
    fun `Space에 Content 추가할때 Content의 Space 에 등록이 되어있는지 확인한다`() {
        // given
        val contentEntity = createSampleContentEntity()

        // when
        spaceService.addContent(space, contentEntity)

        // then
        assertEquals(space, contentEntity.spaceEntity)
    }
}

@SpringBootTest
@Import(TestConfig::class)
class SpaceServiceImplTest {

    @Autowired
    lateinit var spaceRepository: SpaceRepository
    @Autowired
    lateinit var contentService: ContentService
    @Autowired
    lateinit var contentRepository: ContentRepository
    @Autowired
    lateinit var spaceService: SpaceService

    lateinit var space: SpaceEntity

    @BeforeEach
    fun setUp() {
        space = createSampleSpaceEntity()
        spaceRepository.save(space)
    }

    @AfterEach
    fun tearDown() {
        contentRepository.deleteAll()
        spaceRepository.deleteAll()
    }

    @Test
    fun `Space 를 ID 로 찾을 때 존재하지 않는다면 SpaceNotFoundException 을 던진다`() {
        // given
        val nonExistId = getNonExistIdForSpaceEntity(spaceRepository)

        // when, then
        assertThrows<SpaceNotFoundException> {
            spaceService.findById(nonExistId)
        }
    }

    @Test
    fun `Space 를 ID 로 찾는다`() {
        // given
        val spaceId = space.id!!

        // when
        val result = spaceService.findById(spaceId)

        // then
        assertEquals(spaceId, result.id!!)
    }

    @Test
    fun `Space 를 삭제할 때 ID 가 존재하지 않는다면 아무일도 일어나지 않는다`() {
        // given
        val nonIdSpace = createSampleSpaceEntity()

        val beforeNumOfSpace = spaceRepository.count()
        val beforeNumOfContent = contentRepository.count()

        // when
        spaceService.delete(nonIdSpace)

        // then
        assertEquals(beforeNumOfSpace, spaceRepository.count())
        assertEquals(beforeNumOfContent, contentRepository.count())
    }

    @Test
    fun `Space 를 삭제한다`() {
        // given
        val contentForSpace = createSampleContentEntity()
        contentForSpace.spaceEntity = space
        contentRepository.save(contentForSpace)

        val spaceId = space.id!!
        val contentId = contentForSpace.id!!

        // when
        spaceService.delete(space)

        // then
        assertFalse(spaceRepository.existsById(spaceId))
        assertFalse(contentRepository.existsById(contentId))
    }

    @Test
    fun `Space 에 Content 를 추가할 때 최대 갯수를 넘는다면 ContentNumLimitExceedException 를 던진다`() {
        // given
        addContentUntilMaxNumOfContent(space)

        val beforeNumOfContent = contentRepository.count()

        // when, then
        assertThrows<ContentNumLimitExceedException> {
            spaceService.addContent(space, createSampleContentEntity())
        }

        assertEquals(beforeNumOfContent, contentRepository.count())
    }

    private fun addContentUntilMaxNumOfContent(space: SpaceEntity) {
        for (i in 0 until SpaceServiceImpl.MAX_CONTENT_NUM_PER_SPACE) {
            createSampleContentEntity().apply {
                spaceEntity = space
                contentRepository.save(this)
            }
        }
    }

    @Test
    fun `Space 에 Content 를 추가한다`() {
        // given
        val newContentForSpace = createSampleContentEntity()

        val beforeNumOfContent = contentRepository.count()

        // when
        spaceService.addContent(space, newContentForSpace)

        // then
        assertEquals(beforeNumOfContent + 1, contentRepository.count())
    }
}