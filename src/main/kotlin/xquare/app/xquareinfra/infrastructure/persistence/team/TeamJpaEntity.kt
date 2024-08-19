package xquare.app.xquareinfra.infrastructure.persistence.team

import xquare.app.xquareinfra.domain.BaseUUIDEntity
import xquare.app.xquareinfra.domain.model.domain.UserTeam
import xquare.app.xquareinfra.domain.model.domain.type.TeamType
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "tbl_team")
class TeamJpaEntity(
    id: UUID?,
    adminId: UUID,
    teamNameKo: String,
    teamNameEn: String,
    teamType: TeamType,
) : BaseUUIDEntity(id) {
    @Column(name = "admin_name", nullable = false, columnDefinition = "BINARY(16)")
    var adminId: UUID = adminId
        protected set

    @Column(name = "team_name_ko", nullable = false)
    var teamNameKo: String = teamNameKo
        protected set

    @Column(name = "team_name_en", nullable = false)
    var teamNameEn: String = teamNameEn
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "team_type", nullable = false)
    var teamType: TeamType = teamType
        protected set

    @OneToMany(mappedBy = "teamJpaEntity", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val members: MutableSet<UserTeam> = HashSet()

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now()
}