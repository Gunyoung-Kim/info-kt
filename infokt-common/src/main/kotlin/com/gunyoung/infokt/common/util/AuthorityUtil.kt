package com.gunyoung.infokt.common.util

import org.springframework.security.core.context.SecurityContextHolder

fun getSessionUserEmail(): String = SecurityContextHolder.getContext().authentication.name