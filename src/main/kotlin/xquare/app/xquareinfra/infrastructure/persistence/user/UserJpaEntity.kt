package xquare.app.xquareinfra.infrastructure.persistence.user

import xquare.app.xquareinfra.domain.BaseUUIDEntity
import xquare.app.xquareinfra.infrastructure.persistence.team.TeamJpaEntity
import xquare.app.xquareinfra.domain.team.model.UserTeam
import xquare.app.xquareinfra.domain.team.model.role.TeamMemberRole
import xquare.app.xquareinfra.domain.user.model.Role
import xquare.app.xquareinfra.domain.user.model.converter.RoleConverter
import java.util.UUID
import javax.persistence.*

@Entity(name = "tbl_user")
class UserJpaEntity(
    id: UUID?,
    name: String,
    accountId: String,
    grade: Int,
    classNum: Int,
    number: Int,
    roles: MutableList<Role>,
    email: String
) : BaseUUIDEntity(id) {
    @Column(name = "name", nullable = false)
    var name: String = name
        protected set

    @Column(name = "account_id", nullable = false, unique = true)
    var accountId: String = accountId
        protected set

    @Column(name = "grade", nullable = false)
    var grade: Int = grade
        protected set

    @Column(name = "class_num", nullable = false)
    var classNum: Int = classNum
        protected set

    @Column(name = "number", nullable = false)
    var number: Int = number
        protected set

    @Convert(converter = RoleConverter::class)
    @Column(name = "roles", length = 15, nullable = false)
    var roles: MutableList<Role> = roles
        protected set

    @OneToMany(mappedBy = "userJpaEntity", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    val teams: MutableSet<UserTeam> = HashSet()

    @Column(name = "email", nullable = false)
    var email: String = email
        protected set

    fun addTeam(teamJpaEntity: TeamJpaEntity, teamMemberRole: TeamMemberRole) {
        val userTeam = UserTeam(userJpaEntity = this, teamJpaEntity = teamJpaEntity, teamMemberRole = teamMemberRole)
        teams.add(userTeam)
        teamJpaEntity.members.add(userTeam)
    }

    fun deleteTeam(userTeam: UserTeam) {
        teams.remove(userTeam)
    }
}