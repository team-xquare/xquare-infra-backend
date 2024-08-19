package xquare.app.xquareinfra.domain.team.model

import xquare.app.xquareinfra.domain.team.model.role.TeamMemberRole
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import javax.persistence.*

@Entity
class UserTeam(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val userJpaEntity: UserJpaEntity,

    @ManyToOne
    @JoinColumn(name = "team_id")
    val teamJpaEntity: TeamJpaEntity,

    val teamMemberRole: TeamMemberRole
)