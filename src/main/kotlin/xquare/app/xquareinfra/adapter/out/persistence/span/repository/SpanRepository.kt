package xquare.app.xquareinfra.adapter.out.persistence.span.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import xquare.app.xquareinfra.infrastructure.persistence.span.SpanMongoEntity

interface SpanRepository : MongoRepository<SpanMongoEntity, String> {
    @Query("{'startTimeUnixNano': {\$gte: ?0, \$lte: ?1}, 'endTimeUnixNano': {\$gte: ?0, \$lte: ?1}, 'rootServiceName': ?2}")
    fun findSpansBetweenTimesByRootServiceName(
        startTimeUnixNano: Long,
        endTimeUnixNano: Long,
        rootServiceName: String
    ): List<SpanMongoEntity>
}