package xquare.app.xquareinfra.adapter.out.persistence.trace.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOptions
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Repository
import xquare.app.xquareinfra.adapter.out.persistence.trace.SpanMapper
import xquare.app.xquareinfra.domain.trace.model.Span
import xquare.app.xquareinfra.domain.trace.model.Trace
import xquare.app.xquareinfra.infrastructure.persistence.trace.SpanMongoEntity

@Repository
class SpanRepository(
    private val mongoTemplate: MongoTemplate,
    private val spanMapper: SpanMapper
) {
    fun findSpansServiceNameWithParentIdAndDateNanoBetween(
        serviceName: String,
        startTimeUnix: Long,
        endTimeUnix: Long,
        parentSpanId: String?
    ): List<Span> {
        val aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("serviceName").`is`(serviceName)
                .and("startTimeUnixNano").gte(startTimeUnix).lte(endTimeUnix)
                .and("parentSpanId").`is`(parentSpanId)
            ),
        ).withOptions(AggregationOptions.builder().cursorBatchSize(100).build())

        val results: AggregationResults<SpanMongoEntity> = mongoTemplate.aggregate(aggregation, "spans", SpanMongoEntity::class.java)
        return results.map { spanMapper.toModel(it) }
    }

    fun findSpansByServiceNameWithDateNanoBetween(
        serviceName: String,
        startTimeUnix: Long,
        endTimeUnix: Long,
    ): List<Span> {
        val aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("serviceName").`is`(serviceName)
                .and("startTimeUnixNano").gte(startTimeUnix).lte(endTimeUnix)
            ),
        ).withOptions(AggregationOptions.builder().cursorBatchSize(100).build())

        val results: AggregationResults<SpanMongoEntity> = mongoTemplate.aggregate(aggregation, "spans", SpanMongoEntity::class.java)
        return results.map { spanMapper.toModel(it) }
    }

    fun findTracesByServiceNameWithDateNanoBetween(
        serviceName: String,
        startTimeUnix: Long,
        endTimeUnix: Long
    ): List<Trace> {
        val aggregation = Aggregation.newAggregation(
            Aggregation.match(
                Criteria.where("serviceName").`is`(serviceName)
                    .and("startTimeUnixNano").gte(startTimeUnix).lte(endTimeUnix)
            ),
            Aggregation.group("traceId")
                .push("\$\$ROOT").`as`("spans")
                .first("serviceName").`as`("serviceName")
                .min("endTimeUnixNano").`as`("startTimeUnixNano")
                .max("endTimeUnixNano").`as`("endTimeUnixNano"),
            Aggregation.project()
                .and("_id").`as`("traceId")
                .and("spans").`as`("spans")
                .and("serviceName").`as`("serviceName")
                .and("startTimeUnixNano").`as`("startTimeUnixNano")
                .and("endTimeUnixNano").`as`("endTimeUnixNano")
        ).withOptions(AggregationOptions.builder().cursorBatchSize(100).build())

        val results: AggregationResults<TraceAggregationResult> = mongoTemplate.aggregate(
            aggregation,
            "spans",
            TraceAggregationResult::class.java
        )

        return results.mappedResults.map { aggregationResult ->
            Trace(
                traceId = aggregationResult.traceId,
                spans = aggregationResult.spans.map { spanMapper.toModel(it) },
                serviceName = aggregationResult.serviceName,
                startTimeUnixNano = aggregationResult.startTimeUnixNano,
                endTimeUnixNano = aggregationResult.endTimeUnixNano
            )
        }
    }

    fun findTraceByTraceId(traceId: String): Trace? {
        val aggregation = Aggregation.newAggregation(
            Aggregation.match(
                Criteria.where("traceId").`is`(traceId)
            ),
            Aggregation.group("traceId")
                .push("\$\$ROOT").`as`("spans")
                .first("serviceName").`as`("serviceName")
                .min("startTimeUnixNano").`as`("startTimeUnixNano")
                .max("endTimeUnixNano").`as`("endTimeUnixNano"),
            Aggregation.project()
                .and("_id").`as`("traceId")
                .and("spans").`as`("spans")
                .and("serviceName").`as`("serviceName")
                .and("startTimeUnixNano").`as`("startTimeUnixNano")
                .and("endTimeUnixNano").`as`("endTimeUnixNano")
        ).withOptions(AggregationOptions.builder().cursorBatchSize(100).build())

        val results: AggregationResults<TraceAggregationResult> = mongoTemplate.aggregate(
            aggregation,
            "spans",
            TraceAggregationResult::class.java
        )

        val aggregationResult = results.uniqueMappedResult ?: return null

        return Trace(
            traceId = aggregationResult.traceId,
            spans = aggregationResult.spans.map { spanMapper.toModel(it) },
            serviceName = aggregationResult.serviceName,
            startTimeUnixNano = aggregationResult.startTimeUnixNano,
            endTimeUnixNano = aggregationResult.endTimeUnixNano
        )
    }

    data class TraceAggregationResult(
        @Field("traceId") val traceId: String,
        @Field("spans") val spans: List<SpanMongoEntity>,
        @Field("serviceName") val serviceName: String?,
        @Field("startTimeUnixNano") val startTimeUnixNano: Long,
        @Field("endTimeUnixNano") val endTimeUnixNano: Long,
    )
}