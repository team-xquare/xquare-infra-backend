package xquare.app.xquareinfra.adapter.out.persistence.trace

<<<<<<< HEAD
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.scheduling.annotation.Async
=======
>>>>>>> trace
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
<<<<<<< HEAD
    private val traceRepository: TraceRepository,
    private val mongoTemplate: MongoTemplate
=======
    private val traceRepository: TraceRepository
>>>>>>> trace
): SaveTracePort, FindTracePort {
    override fun save(trace: Trace): Trace {
        return traceMapper.toModel(traceRepository.save(traceMapper.toEntity(trace)))
    }

<<<<<<< HEAD
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
=======
    override fun findTracesByServiceNameInTimeRange(
        serviceName: String,
        startTimeNano: Long,
        endTimeNano: Long
    ): List<Trace> {
        return traceRepository.findAllByServiceNameInTimeRange(serviceName, startTimeNano, endTimeNano).map { traceMapper.toModel(it) }
>>>>>>> trace
    }
}