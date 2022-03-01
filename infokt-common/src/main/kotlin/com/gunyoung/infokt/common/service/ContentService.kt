package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.code.ContentErrorCode
import com.gunyoung.infokt.common.model.ContentEntity
import com.gunyoung.infokt.common.model.ContentNotFoundException
import com.gunyoung.infokt.common.repository.ContentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ContentService {
    fun findById(id: Long): ContentEntity
    fun findByIdWithSpaceAndUser(id: Long): ContentEntity
    fun findByIdWithLinks(id: Long): ContentEntity
    fun findAllBySpaceIdWithLinks(spaceId: Long): List<ContentEntity>

    fun delete(contentEntity: ContentEntity)
    fun deleteById(id: Long)
    fun deleteAllBySpaceId(spaceId: Long)

    fun countAll(): Long
    fun existsById(id: Long): Boolean
}

@Service
class ContentServiceImpl(
    val contentRepository: ContentRepository,
    val linkService: LinkService
) : ContentService {
    override fun findById(id: Long): ContentEntity =
        contentRepository.findById(id)
            .orElseThrow { ContentNotFoundException(ContentErrorCode.CONTENT_NOT_FOUNDED_ERROR.description) }

    override fun findByIdWithSpaceAndUser(id: Long): ContentEntity =
        contentRepository.findByIdWithSpaceAndPerson(id)
            ?: throw ContentNotFoundException(ContentErrorCode.CONTENT_NOT_FOUNDED_ERROR.description)

    override fun findByIdWithLinks(id: Long): ContentEntity =
        contentRepository.findByIdWithLinks(id)
            ?: throw ContentNotFoundException(ContentErrorCode.CONTENT_NOT_FOUNDED_ERROR.description)

    override fun findAllBySpaceIdWithLinks(spaceId: Long): List<ContentEntity> =
        contentRepository.findAllBySpaceIdWithLinks(spaceId)

    @Transactional
    override fun delete(contentEntity: ContentEntity) {
        deleteAllLinksForContent(contentEntity)
        contentRepository.delete(contentEntity)
    }

    @Transactional
    override fun deleteById(id: Long) = delete(findById(id))

    @Transactional
    override fun deleteAllBySpaceId(spaceId: Long) {
        deleteAllLinksForSpaceContents(contentRepository.findAllBySpaceIdInQuery(spaceId))
    }

    private fun deleteAllLinksForSpaceContents(contentsForSpace: List<ContentEntity>) {
        contentsForSpace.forEach {
            deleteAllLinksForContent(it)
        }
    }

    private fun deleteAllLinksForContent(contentEntity: ContentEntity) {
        contentEntity.id?.let { linkService.deleteAllByContentId(it) }
    }

    override fun countAll(): Long = contentRepository.count()

    override fun existsById(id: Long): Boolean = contentRepository.existsById(id)
}