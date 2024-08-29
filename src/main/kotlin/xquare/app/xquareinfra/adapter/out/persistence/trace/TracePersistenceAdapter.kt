package xquare.app.xquareinfra.adapter.out.persistence.trace

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.trace.repository.TraceRepository
import xquare.app.xquareinfra.application.trace.port.out.FindTracePort
import xquare.app.xquareinfra.application.trace.port.out.SaveTracePort
import xquare.app.xquareinfra.domain.trace.model.Trace

@Component
class TracePersistenceAdapter(
    private val traceMapper: TraceMapper,
    private val traceRepository: TraceRepository
): SaveTracePort, FindTracePort {
    override fun save(trace: Trace): Trace {
        return traceMapper.toModel(traceRepository.save(traceMapper.toEntity(trace)))
    }

    override fun findTracesByServiceNameInTimeRange(
        serviceName: String,
        startTimeNano: Long,
        endTimeNano: Long
    ): List<Trace> {
        println("serviceName: $serviceName\nstartTimeNano: $startTimeNano\nendTimeNano: $endTimeNano")
        return traceRepository.findAllByServiceNameInTimeRange(
            serviceName = serviceName,
            startTimeUnix = startTimeNano,
            endTimeUnix = endTimeNano
        ).map { traceMapper.toModel(it) }
    }
}