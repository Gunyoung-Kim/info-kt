package com.gunyoung.infokt.common.repository

import com.gunyoung.infokt.common.model.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?

    @Query(
        "SELECT u FROM UserEntity u "
                + "INNER JOIN FETCH u.spaceEntity s "
                + "WHERE u.id = :userId"
    )
    fun findByIdWithSpaceInCustom(@Param("userId") id: Long): UserEntity?

    @Query(
        "SELECT u FROM UserEntity u "
                + "INNER JOIN FETCH u.spaceEntity s "
                + "WHERE u.email = :email"
    )
    fun findByEmailWithSpaceInCustom(@Param("email") email: String): UserEntity?

    @Query(
        "SELECT u FROM UserEntity u "
                + "INNER JOIN FETCH u.spaceEntity s "
                + "LEFT JOIN FETCH s.contentEntities c "
                + "WHERE u.id = :userId"
    )
    fun findByIdWithSpaceAndContentsInCustom(@Param("userId") userId: Long): UserEntity?

    @Query(
        "SELECT u FROM UserEntity u "
                + "INNER JOIN FETCH u.spaceEntity s "
                + "LEFT JOIN FETCH s.contentEntities c "
                + "WHERE u.email = :email"
    )
    fun findByEmailWithSpaceAndContentsInCustom(@Param("email") email: String): UserEntity?

    @Query(
        "SELECT u FROM UserEntity u "
                + "WHERE u.firstName LIKE %:keyword% "
                + "OR u.lastName LIKE %:keyword% "
    )
    fun findByNameWithKeyword(@Param("keyword") keyword: String, pageable: Pageable): Page<UserEntity>

    fun findAllByOrderByCreatedAtDesc(): List<UserEntity>

    fun findAllByOrderByCreatedAtDesc(pageable: Pageable?): Page<UserEntity>

    fun findAllByOrderByCreatedAtAsc(pageable: Pageable?): Page<UserEntity>

    @Query(
        "SELECT COUNT(u) FROM UserEntity u "
                + "WHERE u.firstName LIKE %:keyword% "
                + "OR u.lastName LIKE %:keyword% "
    )
    fun countWithNameKeyword(@Param("keyword") keyword: String): Long

    fun existsByEmail(email: String): Boolean
}