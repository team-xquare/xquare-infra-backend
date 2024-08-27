package xquare.app.xquareinfra.adapter.out.persistence.trace.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import xquare.app.xquareinfra.infrastructure.persistence.trace.TraceMongoEntity

interface TraceRepository : MongoRepository<TraceMongoEntity, String> {
    @Query("{ 'serviceName': ?0, 'dateNano': { \$gte: ?1, \$lte: ?2 } }")
    fun findTraceListByServiceNameInTimeRange(
        serviceName: String,
        startTime: Long,
        endTime: Long
    ): List<TraceMongoEntity>
}