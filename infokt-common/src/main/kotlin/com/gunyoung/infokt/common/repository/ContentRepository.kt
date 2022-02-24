package com.gunyoung.infokt.common.repository

import com.gunyoung.infokt.common.model.ContentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ContentRepository : JpaRepository<ContentEntity, Long> {

    @Query(
        "SELECT c FROM ContentEntity c "
                + "INNER JOIN FETCH c.spaceEntity s "
                + "INNER JOIN FETCH s.userEntity u "
                + "WHERE c.id = :contentId"
    )
    fun findByIdWithSpaceAndPerson(@Param("contentId") contentId: Long): ContentEntity?

    @Query(
        "SELECT c FROM ContentEntity c "
                + "LEFT JOIN FETCH c.linkEntities l "
                + "WHERE c.id = :contentId"
    )
    fun findByIdWithLinks(@Param("contentId") contentId: Long): ContentEntity?

    @Query(
        "SELECT c FROM ContentEntity c "
                + "INNER JOIN c.spaceEntity s "
                + "WHERE s.id = :spaceId"
    )
    fun findAllBySpaceIdInQuery(@Param("spaceId") spaceId: Long): List<ContentEntity>

    @Query(
        "SELECT DISTINCT c FROM ContentEntity c "
                + "LEFT JOIN FETCH c.linkEntities l "
                + "INNER JOIN c.spaceEntity s "
                + "WHERE s.id = :spaceId"
    )
    fun findAllBySpaceIdWithLinks(@Param("spaceId") spaceId: Long): List<ContentEntity>

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        "DELETE FROM ContentEntity c "
                + "WHERE c.spaceEntity.id = :spaceId"
    )
    fun deleteAllBySpaceIdInQuery(@Param("spaceId") spaceId: Long)
}