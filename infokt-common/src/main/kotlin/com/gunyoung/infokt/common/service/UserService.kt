package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.code.UserErrorCode
import com.gunyoung.infokt.common.model.UserEntity
import com.gunyoung.infokt.common.model.UserJoinDto
import com.gunyoung.infokt.common.model.UserMapper
import com.gunyoung.infokt.common.model.UserNotFoundException
import com.gunyoung.infokt.common.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UserService {
    fun findById(id: Long): UserEntity
    fun findByIdWithSpace(id: Long): UserEntity
    fun findByEmail(email: String): UserEntity
    fun findByEmailWithSpace(email: String): UserEntity
    fun findByIdWithSpaceAndContents(id: Long): UserEntity
    fun findByEmailWithSpaceAndContents(email: String): UserEntity

    fun findAll(): List<UserEntity>
    fun findAllOrderByCreatedAtDesc(): List<UserEntity>
    fun findAllInPage(pageNumber: Int): Page<UserEntity>
    fun findAllOrderByCreatedAtDescInPage(pageNumber: Int): Page<UserEntity>
    fun findAllOrderByCreatedAtAscInPage(pageNumber: Int): Page<UserEntity>
    fun findByNameKeywordInPage(pageNumber: Int, keyword: String): Page<UserEntity>

    fun createNewUser(userJoinDto: UserJoinDto): UserEntity

    fun delete(userEntity: UserEntity)
    fun deleteByEmail(email: String)

    fun existsByEmail(email: String): Boolean

    fun countAll(): Long
    fun countWithNameKeyword(keyword: String): Long
}

@Service
class UserServiceImpl(
    val userRepository: UserRepository,
    val spaceService: SpaceService,
    val userMapper: UserMapper,
    val passwordEncoder: PasswordEncoder
) : UserService {

    companion object {
        const val PAGE_SIZE = 10
    }

    override fun findById(id: Long): UserEntity =
        userRepository.findById(id)
            .orElseThrow { UserNotFoundException(UserErrorCode.USER_NOT_FOUNDED_ERROR.description) }

    override fun findByIdWithSpace(id: Long): UserEntity =
        userRepository.findByIdWithSpaceInCustom(id)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUNDED_ERROR.description)

    override fun findByEmail(email: String): UserEntity =
        userRepository.findByEmail(email)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUNDED_ERROR.description)

    override fun findByEmailWithSpace(email: String): UserEntity =
        userRepository.findByEmailWithSpaceInCustom(email)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUNDED_ERROR.description)

    override fun findByIdWithSpaceAndContents(id: Long): UserEntity =
        userRepository.findByIdWithSpaceAndContentsInCustom(id)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUNDED_ERROR.description)

    override fun findByEmailWithSpaceAndContents(email: String): UserEntity =
        userRepository.findByEmailWithSpaceAndContentsInCustom(email)
            ?: throw UserNotFoundException(UserErrorCode.USER_NOT_FOUNDED_ERROR.description)

    override fun findAll(): List<UserEntity> =
        userRepository.findAll()

    override fun findAllOrderByCreatedAtDesc(): List<UserEntity> =
        userRepository.findAllByOrderByCreatedAtDesc()

    override fun findAllInPage(pageNumber: Int): Page<UserEntity> =
        userRepository.findAll(
            PageRequest.of(pageNumber - 1, PAGE_SIZE)
        )

    override fun findAllOrderByCreatedAtDescInPage(pageNumber: Int): Page<UserEntity> =
        userRepository.findAllByOrderByCreatedAtDesc(
            PageRequest.of(pageNumber - 1, PAGE_SIZE)
        )

    override fun findAllOrderByCreatedAtAscInPage(pageNumber: Int): Page<UserEntity> =
        userRepository.findAllByOrderByCreatedAtAsc(
            PageRequest.of(pageNumber - 1, PAGE_SIZE)
        )

    override fun findByNameKeywordInPage(pageNumber: Int, keyword: String): Page<UserEntity> =
        userRepository.findByNameWithKeyword(keyword, PageRequest.of(pageNumber-1, PAGE_SIZE))

    override fun createNewUser(userJoinDto: UserJoinDto): UserEntity =
        userMapper.userJoinDtoToEntity(userJoinDto)
            .encodePassword()
            .save()

    private fun UserEntity.encodePassword() = apply {
        password = passwordEncoder.encode(password)
    }

    private fun UserEntity.save() = userRepository.save(this)

    @Transactional
    override fun delete(userEntity: UserEntity) {
        userRepository.delete(userEntity)
        spaceService.delete(userEntity.spaceEntity)
    }

    @Transactional
    override fun deleteByEmail(email: String) = delete(findByEmail(email))

    override fun existsByEmail(email: String): Boolean =
        userRepository.existsByEmail(email)

    override fun countAll(): Long =
        userRepository.count()

    override fun countWithNameKeyword(keyword: String): Long =
        userRepository.countWithNameKeyword(keyword)


}