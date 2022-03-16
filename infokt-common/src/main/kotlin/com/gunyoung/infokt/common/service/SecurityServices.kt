package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.repository.UserRepository
import com.gunyoung.infokt.common.util.getSessionUserEmail

interface SecurityService {
}

interface UserSecurityService {
    fun checkIsMineByEmail(email: String): Boolean
}

class UserSecurityServiceImpl(val userRepository: UserRepository) : UserSecurityService {
    override fun checkIsMineByEmail(email: String): Boolean {
        return getSessionUserEmail().let {
            checkExistEmail(it) && it == email
        }
    }

    private fun checkExistEmail(email: String): Boolean = userRepository.existsByEmail(email)
}