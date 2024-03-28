package xquare.app.xquareinfra.domain.team.domain

import xquare.app.xquareinfra.domain.team.domain.role.TeamMemberRole
import xquare.app.xquareinfra.domain.user.domain.User
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
    val team: Team,

    val teamMemberRole: TeamMemberRole
)