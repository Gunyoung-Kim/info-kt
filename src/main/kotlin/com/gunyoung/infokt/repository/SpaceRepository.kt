package com.gunyoung.infokt.repository

import com.gunyoung.infokt.domain.SpaceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SpaceRepository : JpaRepository<SpaceEntity, Long> {

    @Query(
        "SELECT s FROM SpaceEntity s "
                + "INNER JOIN s.userEntity u "
                + "WHERE u.id = :userId"
    )
    fun findByPersonIdInQuery(@Param("userId") userId: Long): SpaceEntity?

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        "DELETE FROM SpaceEntity s "
                + "WHERE s.id = :spaceId"
    )
    fun deleteByIdInQuery(@Param("spaceId") spaceId: Long)

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        "DELETE FROM SpaceEntity s "
                + "WHERE s.userEntity.id = :userId"
    )
    fun deleteByPersonIdInQuery(@Param("userId") userId: Long)
}