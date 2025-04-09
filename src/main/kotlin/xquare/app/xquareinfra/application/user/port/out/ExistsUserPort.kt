package xquare.app.xquareinfra.application.user.port.out

import java.util.UUID

interface ExistsUserPort {
    fun existsById(userId: UUID): Boolean
    fun existsByAccountId(accountId: String): Boolean
}