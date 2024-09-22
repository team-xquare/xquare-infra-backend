package xquare.app.xquareinfra.adapter.out.persistence.trace

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.trace.repository.TraceEventCacheRepository
import xquare.app.xquareinfra.application.trace.port.out.FindTraceEventCachePort
import xquare.app.xquareinfra.application.trace.port.out.SaveTraceEventCachePort
import xquare.app.xquareinfra.infrastructure.listener.TraceEventCache

@Component
class TraceEventCachePersistenceAdapter(
    private val traceEventCacheRepository: TraceEventCacheRepository
): FindTraceEventCachePort, SaveTraceEventCachePort{
    override fun existsById(traceId: String): Boolean {
        return traceEventCacheRepository.existsById(traceId)
    }

    override fun save(traceEventCache: TraceEventCache) {
        traceEventCacheRepository.save(traceEventCache)
    }
}