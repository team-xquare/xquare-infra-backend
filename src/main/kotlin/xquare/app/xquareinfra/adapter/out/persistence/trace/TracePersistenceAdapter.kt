package xquare.app.xquareinfra.adapter.out.persistence.trace

import org.springframework.boot.actuate.trace.http.HttpTraceRepository
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.trace.repository.TraceRepository
import xquare.app.xquareinfra.application.trace.port.out.SaveTracePort
import xquare.app.xquareinfra.domain.trace.model.Trace

@Component
class TracePersistenceAdapter(
    private val traceMapper: TraceMapper,
    private val traceRepository: TraceRepository
): SaveTracePort {
    override fun save(trace: Trace): Trace {
        return traceMapper.toModel(traceRepository.save(traceMapper.toEntity(trace)))
    }
}