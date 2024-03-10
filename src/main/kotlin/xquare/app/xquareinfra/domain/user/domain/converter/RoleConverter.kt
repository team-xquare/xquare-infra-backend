package xquare.app.xquareinfra.domain.user.domain.converter

import xquare.app.xquareinfra.domain.user.domain.Role
import xquare.app.xquareinfra.infrastructure.exception.CriticalException
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
internal class RoleConverter : AttributeConverter<MutableList<Role>, String> {

    private companion object {
        const val SPLIT_CHAR = ","
    }

    override fun convertToDatabaseColumn(attribute: MutableList<Role>): String {
        return attribute.joinToString(SPLIT_CHAR)
    }

    override fun convertToEntityAttribute(dbData: String): MutableList<Role> =
        dbData.split(SPLIT_CHAR).map {
            when (it) {
                Role.USER.name -> Role.USER
                Role.ADMIN.name -> Role.ADMIN
                else -> throw CriticalException(500, "Role Convert Error")
            }
        }.toMutableList()
}
