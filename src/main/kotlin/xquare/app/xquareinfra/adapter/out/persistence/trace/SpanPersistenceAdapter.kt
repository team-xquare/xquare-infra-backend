package xquare.app.xquareinfra.adapter.out.persistence.trace

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.trace.repository.SpanRepository
import xquare.app.xquareinfra.application.trace.port.out.FindSpanPort
import xquare.app.xquareinfra.application.trace.port.out.FindTracePort
import xquare.app.xquareinfra.domain.trace.model.Span
import xquare.app.xquareinfra.domain.trace.model.Trace

@Component
class SpanPersistenceAdapter(
    private val spanRepository: SpanRepository
): FindSpanPort, FindTracePort {
    override fun findSpansByServiceNameInTimeRange(serviceName: String, startTimeNano: Long, endTimeNano: Long): List<Span> {
        return spanRepository.findSpansByServiceNameWithDateNanoBetween(
            serviceName = serviceName,
            startTimeUnix = startTimeNano,
            endTimeUnix = endTimeNano
        )
    }

    override fun findTracesByServiceNameInTimeRange(
        serviceName: String,
        startTimeNano: Long,
        endTimeNano: Long
    ): List<Trace> {
        return spanRepository.findTracesByServiceNameWithDateNanoBetween(
            serviceName = serviceName,
            startTimeUnix = startTimeNano,
            endTimeUnix = endTimeNano,
        )
    }

    override fun findTraceById(traceId: String): Trace? {
        return spanRepository.findTraceByTraceId(traceId)
    }
}