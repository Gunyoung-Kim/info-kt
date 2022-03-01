package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.model.SpaceEntity
import com.gunyoung.infokt.common.model.SpaceNotFoundException
import com.gunyoung.infokt.common.repository.SpaceRepository
import com.gunyoung.infokt.common.util.createSampleContentEntity
import com.gunyoung.infokt.common.util.createSampleSpaceEntity
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
class SpaceServiceImplUnitTest(
    @Mock
    val spaceRepository: SpaceRepository,
    @Mock
    val contentService: ContentService,
    @InjectMocks
    val spaceService: SpaceServiceImpl
) {
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