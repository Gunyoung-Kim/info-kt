package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.code.ContentErrorCode
import com.gunyoung.infokt.common.code.SpaceErrorCode
import com.gunyoung.infokt.common.model.ContentEntity
import com.gunyoung.infokt.common.model.ContentNumLimitExceedException
import com.gunyoung.infokt.common.model.SpaceEntity
import com.gunyoung.infokt.common.model.SpaceNotFoundException
import com.gunyoung.infokt.common.repository.SpaceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    companion object {
        const val MAX_CONTENT_NUM_PER_SPACE = 50
    }

    override fun findById(id: Long): SpaceEntity =
        spaceRepository.findById(id)
            .orElseThrow { SpaceNotFoundException(SpaceErrorCode.SPACE_NOT_FOUND_ERROR.description) }

    override fun findAll(): List<SpaceEntity> =
        spaceRepository.findAll()

    @Transactional
    override fun delete(spaceEntity: SpaceEntity) {
        spaceEntity.id?.let {
            contentService.deleteAllBySpaceId(it)
            spaceRepository.deleteByIdInQuery(it)
        }
    }

    override fun existsById(id: Long): Boolean =
        spaceRepository.existsById(id)

    @Transactional
    override fun addContent(spaceEntity: SpaceEntity, contentEntity: ContentEntity) = spaceEntity.let {
        contentEntity.spaceEntity = it.checkNumOfContent()
    }

    private fun SpaceEntity.checkNumOfContent() : SpaceEntity = apply {
        if (contentEntities.size >= MAX_CONTENT_NUM_PER_SPACE) {
            throw ContentNumLimitExceedException(ContentErrorCode.CONTENT_NUM_LIMIT_EXCEEDED_ERROR.description)
        }
    }
}