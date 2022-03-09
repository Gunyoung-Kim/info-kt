package com.gunyoung.infokt.common.repository

import com.gunyoung.infokt.common.model.SpaceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SpaceRepository : JpaRepository<SpaceEntity, Long> {

    @Query(
        "SELECT s FROM SpaceEntity s "
                + "INNER JOIN s.userEntity u "
                + "WHERE u.id = :userId"
    )
    fun findByPersonIdInQuery(@Param("userId") userId: Long): SpaceEntity?

    @Query(
        "SELECT s FROM SpaceEntity s " +
                "INNER JOIN s.userEntity u " +
                "LEFT JOIN FETCH s.contentEntities e " +
                "WHERE u.id = :userId"
    )
    fun findByUserIdWithContentEntities(@Param("userId") userId: Long): SpaceEntity?

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