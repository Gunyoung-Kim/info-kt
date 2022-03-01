package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.model.UserEntity
import com.gunyoung.infokt.common.model.UserNotFoundException
import com.gunyoung.infokt.common.repository.UserRepository
import com.gunyoung.infokt.common.util.createSampleUserEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
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
        user.id = 1L
    }

    @Test
    fun `ID 를 통해 User 를 찾을 때 존재하지 않는다면 UserNotFoundException 을 던진다`() {
        // given
        val nonExistId = 2L

        given(userRepository.findById(nonExistId)).willReturn(Optional.empty())

        // when, then
        assertThrows<UserNotFoundException> {
            userService.findById(nonExistId)
        }
    }

    @Test
    fun `ID 를 통해 User 를 찾을 때 존재하여 UserEntity 를 반환한다`() {
        // given
        val userId = user.id!!

        given(userRepository.findByIdWithSpaceInCustom(userId)).willReturn(user)

        // when
        val result = userService.findById(userId)

        // then
        assertEquals(user, result)
    }

    @Test
    fun `ID를 통해 User 를 Space 와 페치조인하여 찾을 때 존재하지 않는다면 UserNotFoundException 을 던진다`() {
        // given
        val nonExistId = 2L

        given(userRepository.findByIdWithSpaceInCustom(nonExistId)).willReturn(null)

        // when, then
        assertThrows<UserNotFoundException> {
            userService.findByIdWithSpace(nonExistId)
        }
    }

    @Test
    fun `ID를 통해 User 를 Space 와 페치조인하여 찾을 때 존재하여 UserEntity 를 반환한다`() {
        // given
        val userId = user.id!!

        given(userRepository.findByIdWithSpaceInCustom(userId)).willReturn(user)

        // when
        val result = userService.findByIdWithSpace(userId)

        // then
        assertEquals(user, result)
    }

    @Test
    fun `Email 를 통해 User 를 찾을 때 존재하지 않는다면 UserNotFoundException 을 던진다`() {
        // given
        val nonExistEmail = "nonExist@test.com"

        given(userRepository.findByEmail(nonExistEmail)).willReturn(null)

        // when, then
        assertThrows<UserNotFoundException> {
            userService.findByEmail(nonExistEmail)
        }
    }

    @Test
    fun `Email 를 통해 User 를 찾을 때 존재하여 UserEntity 를 반환한다`() {
        // given
        val userEmail = user.email!!

        given(userRepository.findByEmailWithSpaceInCustom(userEmail)).willReturn(user)

        // when
        val result = userService.findByEmail(userEmail)

        // then
        assertEquals(user, result)
    }

    @Test
    fun `Email를 통해 User 를 Space 와 페치조인하여 찾을 때 존재하지 않는다면 UserNotFoundException 을 던진다`() {
        // given
        val nonExistEmail = "nonExist@test.com"

        given(userRepository.findByEmailWithSpaceInCustom(nonExistEmail)).willReturn(null)

        // when, then
        assertThrows<UserNotFoundException> {
            userService.findByEmailWithSpace(nonExistEmail)
        }
    }

    @Test
    fun `Email를 통해 User 를 Space 와 페치조인하여 찾을 때 존재하여 UserEntity 를 반환한다`() {
        // given
        val userEmail = user.email!!

        given(userRepository.findByEmailWithSpaceInCustom(userEmail)).willReturn(user)

        // when
        val result = userService.findByEmailWithSpace(userEmail)

        // then
        assertEquals(user, result)
    }
    
    @Test
    fun `ID를 통해 User 를 Space 와 Contents 를 페치조인하여 찾을 때 존재하지 않는다면 UserNotFoundedException 을 던진다`() {
        // given
        val nonExistId = 2L
        
        given(userRepository.findByIdWithSpaceAndContentsInCustom(nonExistId)).willReturn(null)
        
        // when, then
        assertThrows<UserNotFoundException> { 
            userService.findByIdWithSpaceAndContents(nonExistId)
        }
    }
    
    @Test
    fun `ID를 통해 User 를 Space와 Contents 를 페치조인하여 찾을 때 존재하여 UserEntity 를 반환한다`() {
        // given
        val userId = user.id!!
        
        given(userRepository.findByIdWithSpaceAndContentsInCustom(userId)).willReturn(user)
        
        // when
        val result = userService.findByIdWithSpaceAndContents(userId)
        
        // then
        assertEquals(user, result)
    }

    @Test
    fun `Email를 통해 User 를 Space 와 Contents 를 페치조인하여 찾을 때 존재하지 않는다면 UserNotFoundedException 을 던진다`() {
        // given
        val nonExistEmail = "nonExist@test.com"

        given(userRepository.findByEmailWithSpaceAndContentsInCustom(nonExistEmail)).willReturn(null)

        // when, then
        assertThrows<UserNotFoundException> {
            userService.findByEmailWithSpaceAndContents(nonExistEmail)
        }
    }

    @Test
    fun `Email를 통해 User 를 Space와 Contents 를 페치조인하여 찾을 때 존재하여 UserEntity 를 반환한다`() {
        // given
        val userEmail = user.email!!

        given(userRepository.findByEmailWithSpaceAndContentsInCustom(userEmail)).willReturn(user)

        // when
        val result = userService.findByEmailWithSpaceAndContents(userEmail)

        // then
        assertEquals(user, result)
    }

    @Test
    fun `모든 User 를 찾는다`() {
        // given
        val allUsers = listOf(user)

        given(userRepository.findAll()).willReturn(allUsers)

        // when
        val result = userService.findAll()

        // then
        assertEquals(allUsers, result)
    }

    @Test
    fun `모든 User 를 생성된 내림차순으로 찾는다`() {
        // given
        val allUsers = listOf(user)

        given(userRepository.findAllByOrderByCreatedAtDesc()).willReturn(allUsers)

        // when
        val result = userService.findAllOrderByCreatedAtDesc()

        // then
        assertEquals(allUsers, result)
    }

    @Test
    fun `모든 User 를 페이지로 찾는다`() {
        // given
        val allUsers = listOf(user)
        val pageNumber = 1

        given(userRepository.findAll(any(PageRequest::class.java))).willReturn(PageImpl(allUsers))

        // when
        val result = userService.findAllInPage(pageNumber)

        // then
        assertEquals(allUsers, result.toList())
    }

    @Test
    fun `User 를 페이지로 생성 내림차순으로 찾는다`() {
        // given
        val allUsers = listOf(user)
        val pageNumber = 1

        given(userRepository.findAllByOrderByCreatedAtDesc(any(PageRequest::class.java))).willReturn(PageImpl(allUsers))

        // when
        val result = userService.findAllOrderByCreatedAtDescInPage(pageNumber)

        // then
        assertEquals(allUsers, result.toList())
    }

    @Test
    fun `User 를 페이지로 생성 오름차순으로 찾는다`() {
        // given
        val allUsers = listOf(user)
        val pageNumber = 1

        given(userRepository.findAllByOrderByCreatedAtAsc(any(PageRequest::class.java))).willReturn(PageImpl(allUsers))

        // when
        val result = userService.findAllOrderByCreatedAtAscInPage(pageNumber)

        // then
        assertEquals(allUsers, result.toList())
    }

    @Test
    fun `User 를 이름 키워드를 통해 페이지로 찾는다`() {
        // given
        val users = listOf(user)
        val keyword = user.firstName!!
        val pageNumber = 1

        given(userRepository.findByNameWithKeyword(any(String::class.java), any(PageRequest::class.java))).willReturn(PageImpl(users))

        // when
        val result = userService.findByNameKeywordInPage(pageNumber, keyword)

        // then
        assertEquals(users, result.toList())
    }

    @Test
    fun `User 를 삭제한다`() {
        // given

        // when
        userService.delete(user)

        // then
        then(userRepository).should(times(1)).delete(user)
        then(spaceService).should(times(1)).delete(user.spaceEntity)
    }

    @Test
    fun `User 를 Email 로 삭제할 때 존재하지 않는 유저라면 UserNotFoundException 을 던진다`() {
        // given
        val nonExistEmail = "nonExist@test.com"

        given(userRepository.findByEmail(nonExistEmail)).willReturn(null)

        // when, then
        assertThrows<UserNotFoundException> {
            userService.deleteByEmail(nonExistEmail)
        }

        then(userRepository).should(never()).delete(any())
    }

    @Test
    fun `User 를 Email 로 삭제`() {
        // given
        val userEmail = user.email!!

        given(userRepository.findByEmail(userEmail)).willReturn(user)

        // when
        userService.deleteByEmail(userEmail)

        // then
        then(userRepository).should(times(1)).delete(user)
        then(spaceService).should(times(1)).delete(user.spaceEntity)
    }

    @Test
    fun `User 를 Email 로 존재하는지 확인하는데 존재하지 않음`() {
        // given
        val nonExistEmail = "nonExist@test.com"
        val isExist = false

        given(userRepository.existsByEmail(nonExistEmail)).willReturn(isExist)

        // when
        val result = userService.existsByEmail(nonExistEmail)

        // then
        assertFalse(result)
    }

    @Test
    fun `User 를 Email 로 존재하는지 확인하는데 존재한다`() {
        // given
        val userEmail = user.email!!
        val isExist = true

        given(userRepository.existsByEmail(userEmail)).willReturn(isExist)

        // when
        val result = userService.existsByEmail(userEmail)

        // then
        assertTrue(result)
    }

    @Test
    fun `모든 User 의 수를 찾는다`() {
        // given
        val userNum = 1L

        given(userRepository.count()).willReturn(userNum)

        // when
        val result = userService.countAll()

        // then
        assertEquals(userNum, result)
    }

    @Test
    fun `이름 키워드를 만족하는 모든 User 의 수를 찾는다`() {
        // given
        val userNum = 1L
        val keyword = user.firstName!!

        given(userRepository.countWithNameKeyword(keyword)).willReturn(userNum)

        // when
        val result = userService.countWithNameKeyword(keyword)

        // then
        assertEquals(userNum, result)
    }
}

@SpringBootTest
class UserServiceImplTest(
) {
}