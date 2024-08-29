package xquare.app.xquareinfra.adapter.out.persistence.trace

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.trace.repository.TraceMongoEntityRepository
import xquare.app.xquareinfra.application.trace.port.out.FindTracePort
import xquare.app.xquareinfra.application.trace.port.out.SaveTracePort
import xquare.app.xquareinfra.domain.trace.model.Trace

@Component
class TracePersistenceAdapter(
    private val traceMapper: TraceMapper,
    private val traceMongoEntityRepository: TraceMongoEntityRepository
): SaveTracePort, FindTracePort {
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
}