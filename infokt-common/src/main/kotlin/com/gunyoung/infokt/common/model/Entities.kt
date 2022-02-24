package com.gunyoung.infokt.common.model

import com.gunyoung.infokt.enum.RoleType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.format.annotation.DateTimeFormat
import java.time.OffsetDateTime
import java.util.*
import java.util.Collections.emptyList
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    @Version @Column
    var version: Int? = null,
    @CreatedDate
    @Column(updatable = false)
    var createdAt: OffsetDateTime? = null,
    @LastModifiedDate
    var modifiedAt: OffsetDateTime? = null
)

@Entity
@Table(name = "user")
class UserEntity(
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "role_type")
    @Enumerated(EnumType.STRING)
    var role: RoleType = RoleType.USER,
    @Column(name = "email", length = 50)
    @NotEmpty
    @Email
    var email: String? = null,
    @NotEmpty
    @Column(name = "password")
    var password: String? = null,
    @NotEmpty
    @Size(max = 60)
    @Column(name = "first_name")
    var firstName: String? = null,
    @NotEmpty
    @Size(max = 60)
    @Column(name = "last_name")
    var lastName: String? = null,
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "space_id")
    var spaceEntity: SpaceEntity = SpaceEntity()
) : BaseEntity() {
    val fullName: String
        get() = "$firstName $lastName"
}

@Entity
@Table(name = "space")
class SpaceEntity(
    @Id
    @Column(name = "space_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "description", columnDefinition = "TEXT")
    var description: String = "",
    @Column(name = "github")
    var github: String = "",
    @Column(name = "instagram")
    var instagram: String = "",
    @Column(name = "twitter")
    var twitter: String = "",
    @Column(name = "facebook")
    var facebook: String = "",
    @OneToOne(mappedBy = "spaceEntity", fetch = FetchType.LAZY)
    var userEntity: UserEntity? = null,
    @OneToMany(mappedBy = "spaceEntity")
    var contentEntities: List<ContentEntity> = emptyList()
)

@Entity
@Table(name = "content")
class ContentEntity(
    @Id
    @Column(name = "content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "title")
    @Size(max = 100)
    var title: String = "",
    @Column(name = "started_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var startedAt: Date? = null,
    @Column(name = "end_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var endAt: Date? = null,
    @Column(name = "skill_stacks")
    var skillStacks: String = "",
    @Column(name = "contributors")
    var contributors: String = "",
    @Column(name = "contents", columnDefinition = "TEXT NOT NULL")
    var contents: String = "",
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    var spaceEntity: SpaceEntity? = null,
    @OneToMany(mappedBy = "contentEntity", orphanRemoval = true)
    var linkEntities: List<LinkEntity> = emptyList()
)

@Entity
@Table(name = "link")
class LinkEntity(
    @Id
    @Column(name = "link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "tag")
    var tag: String = "",
    @Column(name = "url")
    var url: String = "",
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    var contentEntity: ContentEntity? = null
)

