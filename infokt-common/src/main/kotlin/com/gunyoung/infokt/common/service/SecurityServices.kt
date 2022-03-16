package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.model.UserEntity
import com.gunyoung.infokt.common.repository.UserRepository
import com.gunyoung.infokt.common.util.getSessionUserEmail

interface SecurityService {
}

interface UserSecurityService {
    fun checkIsMineById(id: Long): Boolean
    fun checkIsMineByEmail(email: String): Boolean
}

class UserSecurityServiceImpl(val userRepository: UserRepository) : UserSecurityService {
    override fun checkIsMineById(id: Long): Boolean {
        val userEntity = findUserByEmail(getSessionUserEmail()) ?: return false
        return userEntity.id == id
    }

    private fun findUserByEmail(email: String): UserEntity? = userRepository.findByEmail(email)

    override fun checkIsMineByEmail(email: String): Boolean {
        return getSessionUserEmail().let {
            checkExistEmail(it) && it == email
        }
    }

    private fun checkExistEmail(email: String): Boolean = userRepository.existsByEmail(email)
}