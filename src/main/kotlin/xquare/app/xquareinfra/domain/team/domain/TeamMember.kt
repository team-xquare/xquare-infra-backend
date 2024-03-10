package xquare.app.xquareinfra.domain.team.domain

import xquare.app.xquareinfra.domain.BaseUUIDEntity
import xquare.app.xquareinfra.domain.team.domain.role.TeamMemberRole
import java.util.UUID
import javax.persistence.*

@Entity(name = "tbl_team_member")
class TeamMember(
    id: UUID?,
    memberId: UUID,
    teamMemberRole: TeamMemberRole,
    team: Team
) : BaseUUIDEntity(id) {
    @Column(name = "member_id", columnDefinition = "BINARY(16)", nullable = false)
    var memberId: UUID = memberId
        protected set

    @Column(name = "team_member_role", nullable = false)
    @Enumerated(EnumType.STRING)
    var teamMemberRole: TeamMemberRole = teamMemberRole
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", columnDefinition = "BINARY(16)")
    var team: Team = team
        protected set
}