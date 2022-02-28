package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.code.ContentErrorCode
import com.gunyoung.infokt.common.model.ContentEntity
import com.gunyoung.infokt.common.model.ContentNotFoundException
import com.gunyoung.infokt.common.repository.ContentRepository
import org.springframework.stereotype.Service

interface ContentService {
    fun findById(id: Long): ContentEntity
    fun findByIdWithSpaceAndUser(id: Long): ContentEntity
    fun findByIdWithLinks(id: Long): ContentEntity
    fun findAllBySpaceIdWithLinks(spaceId: Long): List<ContentEntity>

    fun delete(contentEntity: ContentEntity)
    fun deleteById(id: Long)

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

    override fun delete(contentEntity: ContentEntity) {
        deleteAllLinksContent(contentEntity)
        contentRepository.delete(contentEntity)
    }

    override fun deleteById(id: Long) = delete(findById(id))

    //todo
    private fun deleteAllLinksContent(contentEntity: ContentEntity) {

    }

    override fun countAll(): Long = contentRepository.count()

    override fun existsById(id: Long): Boolean = contentRepository.existsById(id)
}