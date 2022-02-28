package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.code.LinkErrorCode
import com.gunyoung.infokt.common.model.ContentEntity
import com.gunyoung.infokt.common.model.LinkEntity
import com.gunyoung.infokt.common.model.LinkNotFoundException
import com.gunyoung.infokt.common.model.UpdateLinkDto
import com.gunyoung.infokt.common.repository.LinkRepository
import org.springframework.stereotype.Service
import java.util.*

interface LinkService {
    fun findById(id: Long): LinkEntity
    fun findAllByContentId(contentId: Long): List<LinkEntity>

    fun updateLinksForContent(contentEntity: ContentEntity, updateLinkDtos: Iterable<UpdateLinkDto>) : List<LinkEntity>

    fun delete(linkEntity: LinkEntity)
    fun deleteById(id: Long)
    fun deleteAllByContentId(contentId: Long)
}

@Service
class LinkServiceImpl(
    val linkRepository: LinkRepository
) : LinkService {
    override fun findById(id: Long): LinkEntity =
        linkRepository.findById(id).orElseThrow { LinkNotFoundException(LinkErrorCode.LINK_NOT_FOUND_ERROR.description) }

    override fun findAllByContentId(contentId: Long): List<LinkEntity> =
        linkRepository.findAllByContentId(contentId)

    override fun updateLinksForContent(contentEntity: ContentEntity, updateLinkDtos: Iterable<UpdateLinkDto>) : List<LinkEntity> =
        Collections.emptyList()

    override fun delete(linkEntity: LinkEntity) = linkRepository.delete(linkEntity)

    override fun deleteById(id: Long) = delete(findById(id))

    override fun deleteAllByContentId(contentId: Long) = linkRepository.deleteAllByContentIdInQuery(contentId)
}