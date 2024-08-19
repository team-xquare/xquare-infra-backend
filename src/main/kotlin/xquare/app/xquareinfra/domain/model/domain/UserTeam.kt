package xquare.app.xquareinfra.domain.model.domain

import xquare.app.xquareinfra.domain.model.domain.role.TeamMemberRole
import xquare.app.xquareinfra.domain.user.domain.User
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import javax.persistence.*

@Entity
class UserTeam(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "team_id")
    val teamJpaEntity: TeamJpaEntity,

    val teamMemberRole: TeamMemberRole
)