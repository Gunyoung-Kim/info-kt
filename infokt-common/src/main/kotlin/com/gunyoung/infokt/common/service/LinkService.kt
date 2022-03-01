package com.gunyoung.infokt.common.service

import com.gunyoung.infokt.common.code.LinkErrorCode
import com.gunyoung.infokt.common.model.*
import com.gunyoung.infokt.common.repository.LinkRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface LinkService {
    fun findById(id: Long): LinkEntity
    fun findAllByContentId(contentId: Long): List<LinkEntity>

    fun saveAll(linkEntities: Iterable<LinkEntity>): List<LinkEntity>

    fun updateLinksForContent(contentEntity: ContentEntity, linkUpdateDtos: Iterable<LinkUpdateDto>): List<LinkEntity>

    fun delete(linkEntity: LinkEntity)
    fun deleteById(id: Long)
    fun deleteAllByContentId(contentId: Long)
}

@Service
class LinkServiceImpl(
    val linkRepository: LinkRepository,
    val linkMapper: LinkMapper
) : LinkService {
    override fun findById(id: Long): LinkEntity =
        linkRepository.findById(id)
            .orElseThrow { LinkNotFoundException(LinkErrorCode.LINK_NOT_FOUND_ERROR.description) }

    override fun findAllByContentId(contentId: Long): List<LinkEntity> =
        linkRepository.findAllByContentId(contentId)

    @Transactional
    override fun saveAll(linkEntities: Iterable<LinkEntity>): List<LinkEntity> =
        linkRepository.saveAll(linkEntities)

    @Transactional
    override fun updateLinksForContent(
        contentEntity: ContentEntity,
        linkUpdateDtos: Iterable<LinkUpdateDto>
    ): List<LinkEntity> {
        val linksForSave = arrayListOf<LinkEntity>()
        val linkDtoMapForModifyingLink =
            getLinkDTOMapForModifyingLinkAndAddNewContentLinkToSaveLinkList(linkUpdateDtos, linksForSave, contentEntity)

        modifyOrDeleteExistLinks(
            getIdAndLinkMapForExistLinks(contentEntity.linkEntities),
            linkDtoMapForModifyingLink,
            linksForSave
        )
        return saveAll(linksForSave)
    }

    private fun getLinkDTOMapForModifyingLinkAndAddNewContentLinkToSaveLinkList(
        linkUpdateDtos: Iterable<LinkUpdateDto>,
        linksForSave: ArrayList<LinkEntity>,
        contentEntity: ContentEntity
    ): Map<Long, LinkUpdateDto> {
        val linkDtoMap = hashMapOf<Long, LinkUpdateDto>()
        linkUpdateDtos.forEach {
            when (it.linkId) {
                null -> {
                    val newLinkEntity = linkMapper.linkUpdateDtoToEntity(it)
                    newLinkEntity.contentEntity = contentEntity
                    linksForSave.add(newLinkEntity)
                }
                else -> {
                    linkDtoMap[it.linkId] = it
                }
            }
        }
        return linkDtoMap
    }

    private fun getIdAndLinkMapForExistLinks(existContentLinks: Iterable<LinkEntity>): Map<Long, LinkEntity> {
        val existLinkMap = hashMapOf<Long, LinkEntity>()
        existContentLinks.forEach {
            existLinkMap[it.id!!] = it
        }
        return existLinkMap
    }

    private fun modifyOrDeleteExistLinks(
        idAndLinkMapForExistLinks: Map<Long, LinkEntity>,
        linkDtoMapForModifyingLinkUpdate: Map<Long, LinkUpdateDto>,
        linksForSave: java.util.ArrayList<LinkEntity>
    ) {
        idAndLinkMapForExistLinks.keys.forEach {
            val existLink = idAndLinkMapForExistLinks[it]
            if (linkDtoMapForModifyingLinkUpdate.containsKey(it)) {
                linkMapper.updateEntityFromLinkUpdateDto(existLink!!, linkDtoMapForModifyingLinkUpdate[it]!!)
                linksForSave.add(existLink)
            } else {
                delete(existLink!!)
            }
        }
    }

    @Transactional
    override fun delete(linkEntity: LinkEntity) = linkRepository.delete(linkEntity)

    @Transactional
    override fun deleteById(id: Long) = delete(findById(id))

    @Transactional
    override fun deleteAllByContentId(contentId: Long) = linkRepository.deleteAllByContentIdInQuery(contentId)
}