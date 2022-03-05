package com.gunyoung.infokt.common.model

open class BusinessLogicException(msg: String) : RuntimeException(msg)

open class NotFoundException(msg: String) : BusinessLogicException(msg)
class UserNotFoundException(msg: String) : NotFoundException(msg)
class SpaceNotFoundException(msg: String) : NotFoundException(msg)
class ContentNotFoundException(msg: String) : NotFoundException(msg)
class LinkNotFoundException(msg: String) : NotFoundException(msg)

open class DuplicateException(msg: String) : BusinessLogicException(msg)
class UserEmailDuplicationException(msg: String) : DuplicateException(msg)

open class PermissionDeniedException(msg: String) : BusinessLogicException(msg)
class NotMyResourceException(msg: String) : PermissionDeniedException(msg)

open class BadRequestException(msg: String) : BusinessLogicException(msg)
class ContentNumLimitExceedException(msg: String) : BadRequestException(msg)