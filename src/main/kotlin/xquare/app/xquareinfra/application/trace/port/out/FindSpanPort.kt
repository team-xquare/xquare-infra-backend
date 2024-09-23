package xquare.app.xquareinfra.application.trace.port.out

import xquare.app.xquareinfra.adapter.out.persistence.trace.repository.dto.PersistenceSpanResponse
import xquare.app.xquareinfra.domain.trace.model.Span

interface FindSpanPort {
    fun findSpansByServiceName(
        serviceName: String,
        startTimeNano: Long,
        endTimeNano: Long
    ): List<Span>

    fun findSpansByServiceNameAndDateNanoBeforeWithLimit(
        serviceName: String,
        dateTimeUnix: Long,
        limit: Long
    ): PersistenceSpanResponse

    fun findSpansByServiceNameAndDateNanoAfterWithLimit(
        serviceName: String,
        dateTimeUnix: Long,
        limit: Long
    ): PersistenceSpanResponse

    fun findSpanById(spanId: String): Span?
}