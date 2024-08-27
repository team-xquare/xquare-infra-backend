package xquare.app.xquareinfra.adapter.out.persistence.trace.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import xquare.app.xquareinfra.infrastructure.persistence.trace.SpanMongoEntity

interface SpanRepository : MongoRepository<SpanMongoEntity, String> {
    @Query("{'startTimeUnixNano': {\$lte: ?1}, 'endTimeUnixNano': {\$gte: ?0}, 'rootServiceName': ?2}")
    fun findSpansBetweenTimesByRootServiceName(
        startTimeUnixNano: Long,
        endTimeUnixNano: Long,
        rootServiceName: String
    ): List<SpanMongoEntity>
}