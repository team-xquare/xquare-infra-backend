package xquare.app.xquareinfra.adapter.out.persistence.trace

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import xquare.app.xquareinfra.adapter.out.persistence.trace.repository.TraceRepository
import xquare.app.xquareinfra.application.trace.port.out.FindTracePort
import xquare.app.xquareinfra.application.trace.port.out.SaveTracePort
import xquare.app.xquareinfra.domain.trace.model.Trace
import java.util.concurrent.CompletableFuture

@Component
class TracePersistenceAdapter(
    private val traceMapper: TraceMapper,
    private val traceRepository: TraceRepository,
    private val mongoTemplate: MongoTemplate
): SaveTracePort, FindTracePort {
    override fun save(trace: Trace): Trace {
        return traceMapper.toModel(traceRepository.save(traceMapper.toEntity(trace)))
    }

    override fun findTraceListByServiceNameInTimeRange(
        serviceName: String,
        startTimeUnixNano: Long,
        endTimeUnixNano: Long
    ): List<Trace> {
        return traceRepository.findTraceListByServiceNameInTimeRange(
            serviceName = serviceName,
            startTime = startTimeUnixNano,
            endTime = endTimeUnixNano
        ).map { traceMapper.toModel(it) }
    }
}