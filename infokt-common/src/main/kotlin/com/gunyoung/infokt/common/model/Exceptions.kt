package com.gunyoung.infokt.common.model

open class BusinessLogicException(msg: String) : RuntimeException(msg)

open class NotFoundException(msg: String) : BusinessLogicException(msg)
class UserNotFoundException(msg: String) : NotFoundException(msg)
class SpaceNotFoundException(msg: String) : NotFoundException(msg)
class ContentNotFoundException(msg: String) : NotFoundException(msg)
class LinkNotFoundException(msg: String) : NotFoundException(msg)

open class DuplicateException(msg: String) : BusinessLogicException(msg)
