package xquare.app.xquareinfra.domain.user.domain

import xquare.app.xquareinfra.domain.BaseUUIDEntity
import xquare.app.xquareinfra.domain.user.domain.converter.RoleConverter
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity

@Entity(name = "tbl_user")
class User(
    id: UUID,
    name: String,
    accountId: String,
    grade: Int,
    classNum: Int,
    number: Int,
    roles: MutableList<Role>
) : BaseUUIDEntity(id) {
    @Column(name = "name", nullable = false)
    var name: String = name
        protected set

    @Column(name = "account_id", nullable = false)
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
}