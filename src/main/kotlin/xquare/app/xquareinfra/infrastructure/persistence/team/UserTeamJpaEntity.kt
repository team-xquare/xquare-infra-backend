package xquare.app.xquareinfra.infrastructure.persistence.team

import xquare.app.xquareinfra.domain.team.model.role.TeamMemberRole
import xquare.app.xquareinfra.infrastructure.persistence.user.UserJpaEntity
import javax.persistence.*

@Entity(name = "user_team")
class UserTeamJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserJpaEntity,

    @ManyToOne
    @JoinColumn(name = "team_id")
    var team: TeamJpaEntity,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: TeamMemberRole
)