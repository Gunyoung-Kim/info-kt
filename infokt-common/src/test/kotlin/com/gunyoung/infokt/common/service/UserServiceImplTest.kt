package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.model.UserEntity
import com.gunyoung.infokt.common.model.UserNotFoundException
import com.gunyoung.infokt.common.repository.UserRepository
import com.gunyoung.infokt.common.util.createSampleUserEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceImplUnitTest(
    @Mock
    val userRepository: UserRepository,
    @Mock
    val spaceService: SpaceService,
    @InjectMocks
    val userService: UserServiceImpl
) {
    lateinit var user: UserEntity

    @BeforeEach
    fun setUp() {
        user = createSampleUserEntity()
    }

    @Test
    fun `ID 를 통해 User 를 찾을 때 존재하지 않는다면 UserNotFoundException 을 던진다`() {
        // given
        val nonExistId = 1L

        given(userRepository.findById(nonExistId)).willReturn(Optional.empty())

        // when, then
        assertThrows<UserNotFoundException> {
            userService.findById(nonExistId)
        }
    }

    @Test
    fun `ID 를 통해 User 를 찾을 때 존재하여 UserEntity 를 반환한다`() {
        // given
        val userId = 1L

        given(userRepository.findByIdWithSpaceInCustom(userId)).willReturn(user)

        // when
        val result = userService.findById(userId)

        // then
        assertEquals(user, result)
    }
}