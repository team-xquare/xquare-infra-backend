package xquare.app.xquareinfra.adapter.out.persistence.trace.repository

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOptions
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import xquare.app.xquareinfra.adapter.out.persistence.trace.repository.dto.PersistenceSpanResponse
import xquare.app.xquareinfra.adapter.out.persistence.trace.repository.dto.SpanResult
import xquare.app.xquareinfra.domain.trace.model.Span
import xquare.app.xquareinfra.infrastructure.persistence.trace.TraceMongoEntity


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

    fun findById(id: String): TraceMongoEntity? {
        val query = Query()
        query.addCriteria(Criteria.where("traceId").`is`(id))
        return mongoTemplate.findOne(query, TraceMongoEntity::class.java)
    }

    fun findSpansByServiceNameAndDateNanoBetween(
        serviceName: String,
        startTimeUnix: Long,
        endTimeUnix: Long
    ): List<Span> {
        val aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("serviceName").`is`(serviceName).and("dateNano").gte(startTimeUnix).lte(endTimeUnix)),
            Aggregation.unwind("spans"),
            Aggregation.project().and("spans").`as`("span").andExclude("_id")
        ).withOptions(AggregationOptions.builder().cursorBatchSize(100).build())

        val results: AggregationResults<SpanResult> = mongoTemplate.aggregate(aggregation, "traces", SpanResult::class.java)
        return results.mappedResults.map { it.span }
    }

    fun findSpansByServiceNameAndDateNanoBeforeWithLimit(
        serviceName: String,
        dateTimeUnix: Long,
        limit: Long
    ): PersistenceSpanResponse {
        val fetchLimit = limit + 1

        val aggregation = Aggregation.newAggregation(
            Aggregation.match(
                Criteria.where("serviceName").`is`(serviceName)
                    .and("dateNano").lte(dateTimeUnix)
            ),
            Aggregation.unwind("spans"),
            Aggregation.sort(Sort.by(Sort.Direction.DESC, "spans.dateNano")),
            Aggregation.project()
                .and("spans").`as`("span")
                .andExclude("_id"),
            Aggregation.limit(fetchLimit)
        )

        val results: AggregationResults<SpanResult> = mongoTemplate.aggregate(
            aggregation,
            "traces",
            SpanResult::class.java
        )

        val mappedResults = results.mappedResults.map { it.span }

        val hasMore = mappedResults.size > limit
        val limitedSpans = if (hasMore) mappedResults.take(limit.toInt()) else mappedResults

        return PersistenceSpanResponse(
            spans = limitedSpans,
            hasMore = hasMore
        )
    }

    fun findSpansByServiceNameAndDateNanoAfterWithLimit(
        serviceName: String,
        dateTimeUnix: Long,
        limit: Long
    ): PersistenceSpanResponse {
        val fetchLimit = limit + 1

        val aggregation = Aggregation.newAggregation(
            Aggregation.match(
                Criteria.where("serviceName").`is`(serviceName)
                    .and("dateNano").gte(dateTimeUnix)
            ),
            Aggregation.unwind("spans"),
            Aggregation.sort(Sort.by(Sort.Direction.DESC, "spans.dateNano")),
            Aggregation.project()
                .and("spans").`as`("span")
                .andExclude("_id"),
            Aggregation.limit(fetchLimit)
        )

        val results: AggregationResults<SpanResult> = mongoTemplate.aggregate(
            aggregation,
            "traces",
            SpanResult::class.java
        )

        val mappedResults = results.mappedResults.map { it.span }

        val hasMore = mappedResults.size > limit
        val limitedSpans = if (hasMore) mappedResults.take(limit.toInt()) else mappedResults

        return PersistenceSpanResponse(
            spans = limitedSpans,
            hasMore = hasMore
        )
    }

    fun findSpanBySpanId(spanId: String): Span? {
        val aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("spans.spanId").`is`(spanId)),
            Aggregation.unwind("spans"),
            Aggregation.match(Criteria.where("spans.spanId").`is`(spanId)),
            Aggregation.project().and("spans").`as`("span").andExclude("_id")
        )

        val results: AggregationResults<SpanResult> = mongoTemplate.aggregate(aggregation, "traces", SpanResult::class.java)

        return results.mappedResults.firstOrNull()?.span
    }
}
