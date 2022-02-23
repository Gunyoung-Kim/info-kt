package com.gunyoung.infokt.repository

import com.gunyoung.infokt.domain.LinkEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LinkRepository : JpaRepository<LinkEntity, Long> {
    @Query(
        "SELECT l FROM LinkEntity l "
                + "INNER JOIN l.contentEntity c "
                + "WHERE c.id = :contentId"
    )
    fun findAllByContentId(@Param("contentId") contentId: Long): List<LinkEntity>

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        "DELETE FROM LinkEntity l "
                + "WHERE l.contentEntity.id = :contentId"
    )
    fun deleteAllByContentIdInQuery(@Param("contentId") contentId: Long)
}