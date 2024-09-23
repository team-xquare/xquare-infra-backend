package xquare.app.xquareinfra.adapter.out.persistence.trace

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.trace.repository.TraceMongoEntityRepository
import xquare.app.xquareinfra.adapter.out.persistence.trace.repository.dto.PersistenceSpanResponse
import xquare.app.xquareinfra.application.trace.port.out.FindSpanPort
import xquare.app.xquareinfra.application.trace.port.out.FindTracePort
import xquare.app.xquareinfra.application.trace.port.out.SaveTracePort
import xquare.app.xquareinfra.domain.trace.model.Span
import xquare.app.xquareinfra.domain.trace.model.Trace

@Component
class TracePersistenceAdapter(
    private val traceMapper: TraceMapper,
    private val traceMongoEntityRepository: TraceMongoEntityRepository
): SaveTracePort, FindTracePort, FindSpanPort {
    override fun save(trace: Trace): Trace {
        return traceMapper.toModel(traceMongoEntityRepository.save(traceMapper.toEntity(trace)))
    }

    override fun findTracesByServiceNameInTimeRange(
        serviceName: String,
        startTimeNano: Long,
        endTimeNano: Long
    ): List<Trace> {
        return traceMongoEntityRepository.findByServiceNameAndDateNanoBetween(
            serviceName = serviceName,
            startTimeUnix = startTimeNano,
            endTimeUnix = endTimeNano
        ).map { traceMapper.toModel(it) }
    }

    override fun findTraceById(traceId: String): Trace? {
        return traceMongoEntityRepository.findById(traceId)?.let { traceMapper.toModel(it) }
    }

    override fun findSpansByServiceName(
        serviceName: String,
        startTimeNano: Long,
        endTimeNano: Long
    ): List<Span> {
        return traceMongoEntityRepository.findSpansByServiceNameAndDateNanoBetween(serviceName, startTimeNano, endTimeNano)
    }

    override fun findSpansByServiceNameAndDateNanoBeforeWithLimit(
        serviceName: String,
        dateTimeUnix: Long,
        limit: Long
    ): PersistenceSpanResponse {
        return traceMongoEntityRepository.findSpansByServiceNameAndDateNanoBeforeWithLimit(
            serviceName = serviceName,
            dateTimeUnix = dateTimeUnix,
            limit = limit
        )
    }

    override fun findSpansByServiceNameAndDateNanoAfterWithLimit(
        serviceName: String,
        dateTimeUnix: Long,
        limit: Long
    ): PersistenceSpanResponse {
        return traceMongoEntityRepository.findSpansByServiceNameAndDateNanoAfterWithLimit(
            serviceName = serviceName,
            dateTimeUnix = dateTimeUnix,
            limit = limit
        )
    }

    override fun findSpanById(spanId: String): Span? {
        return traceMongoEntityRepository.findSpanBySpanId(spanId)
    }
}