package xquare.app.xquareinfra.adapter.out.persistence.trace.repository

import org.springframework.data.mongodb.core.MongoTemplate
import xquare.app.xquareinfra.infrastructure.persistence.trace.TraceMongoEntity
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class TraceMongoEntityRepository(
    private val mongoTemplate: MongoTemplate
) {
    fun findByServiceNameAndDateNanoBetween(
        serviceName: String,
        startTimeUnix: Long,
        endTimeUnix: Long
    ): List<TraceMongoEntity> {
        val query = Query()

        query.addCriteria(
            Criteria.where("serviceName").`is`(serviceName)
                .and("dateNano").gte(startTimeUnix).lte(endTimeUnix)
        )

        return mongoTemplate.find(query, TraceMongoEntity::class.java)
    }

    fun save(traceMongoEntity: TraceMongoEntity): TraceMongoEntity {
        return mongoTemplate.save(traceMongoEntity)
    }
}
