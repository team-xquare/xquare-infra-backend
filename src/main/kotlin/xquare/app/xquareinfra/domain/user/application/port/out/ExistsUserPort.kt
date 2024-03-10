package xquare.app.xquareinfra.domain.user.application.port.out

import java.util.UUID

interface ExistsUserPort {
    fun existsById(userId: UUID): Boolean
}