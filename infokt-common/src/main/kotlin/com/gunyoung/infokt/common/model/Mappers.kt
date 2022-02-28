package com.gunyoung.infokt.common.model

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.springframework.security.core.userdetails.User

@Mapper
interface UserMapper {

    fun userJoinDtoToEntity(userJoinDto: UserJoinDto): User

    @Mappings(
        Mapping(target = "userId", source = "userEntity.id"),
        Mapping(target = "userName", source = "userEntity.fullName"),
        Mapping(target = "userEmail", source = "userEntity.email")
    )
    fun entityToSimpleUserInfoDto(userEntity: UserEntity): SimpleUserInfoDto
}