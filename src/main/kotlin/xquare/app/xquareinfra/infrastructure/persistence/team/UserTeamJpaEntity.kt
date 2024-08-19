package xquare.app.xquareinfra.infrastructure.persistence.team

import xquare.app.xquareinfra.domain.team.model.role.TeamMemberRole
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity
import javax.persistence.*

@Entity
class UserTeamJpaEntity(
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