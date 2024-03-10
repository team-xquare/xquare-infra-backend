package xquare.app.xquareinfra.domain.team.domain

import xquare.app.xquareinfra.domain.BaseUUIDEntity
import xquare.app.xquareinfra.domain.team.domain.type.TeamType
import java.util.*
import javax.persistence.*

@Entity(name = "tbl_team")
class Team(
    id: UUID?,
    teamNameKo: String,
    teamNameEn: String,
    teamType: TeamType,
) : BaseUUIDEntity(id) {
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

    @OneToMany(mappedBy = "team", cascade = [CascadeType.REMOVE])
    var teamMemberList: MutableList<TeamMember> = arrayListOf()
        protected set
}