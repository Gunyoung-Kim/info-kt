package com.gunyoung.infokt.common.model

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface UserMapper {

    fun userJoinDtoToEntity(userJoinDto: UserJoinDto): UserEntity

    @Mappings(
        Mapping(target = "userId", source = "userEntity.id"),
        Mapping(target = "userName", source = "userEntity.fullName"),
        Mapping(target = "userEmail", source = "userEntity.email")
    )
    fun entityToSimpleUserInfoDto(userEntity: UserEntity): SimpleUserInfoDto
}

@Mapper
interface LinkMapper {

    @Mappings(
        Mapping(target = "tag", source = "linkDto.linkTag"),
        Mapping(target = "url", source = "linkDto.linkURL")
    )
    fun updateLinkDtoToEntity(linkDto: UpdateLinkDto): LinkEntity
}