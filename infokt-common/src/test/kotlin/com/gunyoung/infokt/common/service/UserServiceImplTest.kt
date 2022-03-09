package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.TestConfig
import com.gunyoung.infokt.common.model.SimpleUserInfoDto
import com.gunyoung.infokt.common.model.UserEntity
import com.gunyoung.infokt.common.model.UserMapper
import com.gunyoung.infokt.common.model.UserNotFoundException
import com.gunyoung.infokt.common.repository.SpaceRepository
import com.gunyoung.infokt.common.repository.UserRepository
import com.gunyoung.infokt.common.util.createSampleUserEntity
import com.gunyoung.infokt.common.util.createSampleUserJoinDto
import com.gunyoung.infokt.common.util.getNonExistIdForUserEntity
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
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceImplUnitTest(
) {
    @Mock
    lateinit var userRepository: UserRepository
    @Mock
    lateinit var spaceService: SpaceService
    @Mock
    lateinit var userMapper: UserMapper
    @Mock
    lateinit var passwordEncoder: PasswordEncoder
    @InjectMocks
    lateinit var userService: UserServiceImpl

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

        given(userRepository.findById(userId)).willReturn(Optional.of(user))

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

        given(userRepository.findByEmail(userEmail)).willReturn(user)

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

        given(userRepository.findByNameWithKeyword(anyString(), any(PageRequest::class.java))).willReturn(PageImpl(users))

        // when
        val result = userService.findByNameKeywordInPage(pageNumber, keyword)

        // then
        assertEquals(users, result.toList())
    }

    @Test
    fun `User 를 생성한다`() {
        // given
        val userEmail = "test@test.com"
        val userJoinDto = createSampleUserJoinDto(userEmail)
        val newUser = createSampleUserEntity(userEmail)
        val encodedPassword = "asgd6a78afghasf"

        given(userMapper.userJoinDtoToEntity(userJoinDto)).willReturn(newUser)
        given(passwordEncoder.encode(userJoinDto.password)).willReturn(encodedPassword)
        given(userRepository.save(newUser)).willReturn(newUser)

        // when
        val result = userService.createNewUser(userJoinDto)

        // then
        assertEquals(newUser, result)
        assertEquals(encodedPassword, result.password)
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
@Import(TestConfig::class)
class UserServiceImplTest {

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var spaceService: SpaceService
    @Autowired
    lateinit var userMapper: UserMapper
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var spaceRepository: SpaceRepository
    @Autowired
    lateinit var contentRepository: SpaceRepository
    @Autowired
    lateinit var linkRepository: SpaceRepository

    @Autowired
    lateinit var userService: UserService

    lateinit var user: UserEntity
    
    @BeforeEach
    fun setUp() {
        user = createSampleUserEntity()
        userRepository.save(user)
    }

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
        spaceRepository.deleteAll()
    }

    @Test
    fun `User 를 ID 로 찾을 때 존재하지 않는다면 UserNotFoundException 을 던진다`() {
        // given
        val nonExistId = getNonExistIdForUserEntity(userRepository)

        // when, then
        assertThrows<UserNotFoundException> {
            userService.findById(nonExistId)
        }
    }

    @Test
    fun `User 를 ID 를 통해 찾는다`() {
        // given
        val userId = user.id!!

        // when
        val result = userService.findById(userId)

        // then
        assertEquals(userId, result.id)
    }

    @Test
    fun `User 를 ID 로 Space 와 페치조인하여 찾을 때 존재하지 않는다면 UserNotFoundException 을 던진다`() {
        // given
        val nonExistId = getNonExistIdForUserEntity(userRepository)

        // when, then
        assertThrows<UserNotFoundException> {
            userService.findByIdWithSpace(nonExistId)
        }
    }

    @Test
    fun `User 를 ID 로 Space 와 페치조인하여 찾는다`() {
        // given
        val userId = user.id!!

        // when
        val result = userService.findByIdWithSpace(userId)

        // then
        assertEquals(userId, result.id)
    }

    @Test
    fun `User 를 Email 로 찾을 때 존재하지 않는다면 UserNotFoundException 을 던진다`() {
        // given
        val nonExistEmail = "nonExist@test.com"

        // when, then
        assertThrows<UserNotFoundException> {
            userService.findByEmail(nonExistEmail)
        }
    }

    @Test
    fun `User 를 Email 로 찾는다`() {
        // given
        val userEmail = user.email!!

        // when
        val result = userService.findByEmail(userEmail)

        // then
        assertEquals(user.id, result.id)
    }

    @Test
    fun `모든 User 들을 찾아서 SimpleUserInfo 로 매핑하여 반환한다`() {
        // given

        // when
        val result = userService.findAllSimpleUserInfo()

        // then
        val simpleUserInfo = result[0]
        verifyUserAndSimpleUserInfoEquility(user, simpleUserInfo)
    }

    private fun verifyUserAndSimpleUserInfoEquility(user: UserEntity, simpleUserInfoDto: SimpleUserInfoDto) {
        assertEquals(user.id, simpleUserInfoDto.userId)
        assertEquals(user.fullName, simpleUserInfoDto.userName)
        assertEquals(user.email, simpleUserInfoDto.userEmail)
    }

    @Test
    fun `새로운 User 를 생성하여 저장한다`() {
        // given
        val newUserEmail = "newUser@test.com"
        val userJoinDto = createSampleUserJoinDto(newUserEmail)

        val beforeNumOfUser = userRepository.count()

        // when
        val result = userService.createNewUser(userJoinDto)

        // then
        assertEquals(beforeNumOfUser + 1, userRepository.count())
        assertEquals(newUserEmail, result.email)
        assertNotEquals(userJoinDto.password, result.password)
    }

    @Test
    fun `User 를 삭제한다`() {
        // given
        val userId = user.id!!
        val spaceId = user.spaceEntity.id!!

        // when
        userService.delete(user)

        // then
        assertFalse(userRepository.existsById(userId))
        assertFalse(spaceRepository.existsById(spaceId))
    }

    @Test
    fun `User 를 Email 로 찾아 삭제할 때 존재하지 않는다면 UserNotFoundException 을 던진다`() {
        // given
        val nonExistEmail = "nonExist@test.com"

        val beforeNumOfUser = userRepository.count()
        val beforeNumOfSpace = spaceRepository.count()

        // when, then
        assertThrows<UserNotFoundException> {
            userService.deleteByEmail(nonExistEmail)
        }

        assertEquals(beforeNumOfUser, userRepository.count())
        assertEquals(beforeNumOfSpace, spaceRepository.count())
    }

    @Test
    fun `User 를 Email 로 찾아 삭제한다`() {
        // given
        val userEmail = user.email!!
        val userId = user.id!!
        val spaceId = user.spaceEntity.id!!

        // when
        userService.deleteByEmail(userEmail)

        // then
        assertFalse(userRepository.existsById(userId))
        assertFalse(spaceRepository.existsById(spaceId))
    }
}