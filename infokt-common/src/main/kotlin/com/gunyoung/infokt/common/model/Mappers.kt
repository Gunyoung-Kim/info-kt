package com.gunyoung.infokt.common.model

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MapperBeanConfig {
    @Bean
    fun userMapper() : UserMapper = Mappers.getMapper(UserMapper::class.java)

    @Bean
    fun linkMapper() : LinkMapper = Mappers.getMapper(LinkMapper::class.java)
}

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
        Mapping(target = "tag", source = "linkUpdateDto.linkTag"),
        Mapping(target = "url", source = "linkUpdateDto.linkURL")
    )
    fun linkUpdateDtoToEntity(linkUpdateDto: LinkUpdateDto): LinkEntity

    @Mappings(
        Mapping(target = "tag", source = "linkUpdateDto.linkTag"),
        Mapping(target = "url", source = "linkUpdateDto.linkURL")
    )
    fun updateEntityFromLinkUpdateDto(@MappingTarget linkEntity: LinkEntity, linkUpdateDto: LinkUpdateDto)
}