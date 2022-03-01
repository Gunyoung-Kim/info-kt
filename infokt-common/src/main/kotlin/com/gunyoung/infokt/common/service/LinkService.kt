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

    fun updateLinksForContent(contentEntity: ContentEntity, updateLinkDtos: Iterable<UpdateLinkDto>): List<LinkEntity>

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
        updateLinkDtos: Iterable<UpdateLinkDto>
    ): List<LinkEntity> {
        val linksForSave = arrayListOf<LinkEntity>()
        val linkDtoMapForModifyingLink =
            getLinkDTOMapForModifyingLinkAndAddNewContentLinkToSaveLinkList(updateLinkDtos, linksForSave, contentEntity)

        modifyOrDeleteExistLinks(
            getIdAndLinkMapForExistLinks(contentEntity.linkEntities),
            linkDtoMapForModifyingLink,
            linksForSave
        )
        return saveAll(linksForSave)
    }

    private fun getLinkDTOMapForModifyingLinkAndAddNewContentLinkToSaveLinkList(
        updateLinkDtos: Iterable<UpdateLinkDto>,
        linksForSave: ArrayList<LinkEntity>,
        contentEntity: ContentEntity
    ): Map<Long, UpdateLinkDto> {
        val linkDtoMap = hashMapOf<Long, UpdateLinkDto>()
        updateLinkDtos.forEach {
            when (it.linkId) {
                null -> {
                    val newLinkEntity = linkMapper.updateLinkDtoToEntity(it)
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
        linkDtoMapForModifyingLink: Map<Long, UpdateLinkDto>,
        linksForSave: java.util.ArrayList<LinkEntity>
    ) {
        idAndLinkMapForExistLinks.keys.forEach {
            val existLink = idAndLinkMapForExistLinks[it]
            if (linkDtoMapForModifyingLink.containsKey(it)) {
                modifyExistLinkByLinkDTO(existLink!!, linkDtoMapForModifyingLink[it]!!)
                linksForSave.add(existLink)
            } else {
                delete(existLink!!)
            }
        }
    }

    private fun modifyExistLinkByLinkDTO(existLink: LinkEntity, updateLinkDto: UpdateLinkDto) {
        existLink.tag = updateLinkDto.linkTag
        existLink.url = updateLinkDto.linkURL
    }

    @Transactional
    override fun delete(linkEntity: LinkEntity) = linkRepository.delete(linkEntity)

    @Transactional
    override fun deleteById(id: Long) = delete(findById(id))

    @Transactional
    override fun deleteAllByContentId(contentId: Long) = linkRepository.deleteAllByContentIdInQuery(contentId)
}