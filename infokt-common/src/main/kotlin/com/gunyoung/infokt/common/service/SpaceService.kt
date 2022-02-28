package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.code.SpaceErrorCode
import com.gunyoung.infokt.common.model.ContentEntity
import com.gunyoung.infokt.common.model.SpaceEntity
import com.gunyoung.infokt.common.model.SpaceNotFoundException
import com.gunyoung.infokt.common.repository.SpaceRepository
import org.springframework.stereotype.Service

interface SpaceService {
    fun findById(id: Long): SpaceEntity
    fun findAll(): List<SpaceEntity>

    fun delete(spaceEntity: SpaceEntity)

    fun existsById(id: Long): Boolean

    fun addContent(spaceEntity: SpaceEntity, contentEntity: ContentEntity)
}

@Service
class SpaceServiceImpl(
    val spaceRepository: SpaceRepository,
    val contentService: ContentService
) : SpaceService {
    override fun findById(id: Long): SpaceEntity =
        spaceRepository.findById(id)
            .orElseThrow { SpaceNotFoundException(SpaceErrorCode.SPACE_NOT_FOUND_ERROR.description) }

    override fun findAll(): List<SpaceEntity> =
        spaceRepository.findAll()

    // todo
    override fun delete(spaceEntity: SpaceEntity) {

    }

    override fun existsById(id: Long): Boolean =
        spaceRepository.existsById(id)

    // todo
    override fun addContent(space: SpaceEntity, contentEntity: ContentEntity) {

    }
}