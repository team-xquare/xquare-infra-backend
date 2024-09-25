package xquare.app.xquareinfra.adapter.out.persistence.trace.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOptions
import org.springframework.data.mongodb.core.aggregation.AggregationResults
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
                .and("dateNano").gte(startTimeUnix).lte(endTimeUnix)
                .and("parentSpanId").`is`(parentSpanId)
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
                    .and("dateNano").gte(startTimeUnix).lte(endTimeUnix)
            ),
            Aggregation.group("traceId")
                .push("\$\$ROOT").`as`("spans")
                .first("serviceName").`as`("serviceName")
                .min("dateNano").`as`("dateNano")
                .sum("durationNano").`as`("durationNano"),
            Aggregation.project()
                .and("_id").`as`("traceId")
                .and("spans").`as`("spans")
                .and("serviceName").`as`("serviceName")
                .and("dateNano").`as`("dateNano")
                .and("durationNano").`as`("durationNano")
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
                dateNano = aggregationResult.dateNano,
                durationNano = aggregationResult.durationNano
            )
        }
    }

    fun findTraceByTraceId(traceId: String): Trace? {
        val aggregation = Aggregation.newAggregation(
            Aggregation.match(
                Criteria.where("traceId").`is`(traceId)
            ),
            Aggregation.group("traceId")
                .push("\$ROOT").`as`("spans")
                .first("serviceName").`as`("serviceName")
                .min("dateNano").`as`("dateNano")
                .sum("durationNano").`as`("durationNano"),
            Aggregation.project()
                .and("_id").`as`("traceId")
                .and("spans").`as`("spans")
                .and("serviceName").`as`("serviceName")
                .and("dateNano").`as`("dateNano")
                .and("durationNano").`as`("durationNano")
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
            dateNano = aggregationResult.dateNano,
            durationNano = aggregationResult.durationNano
        )
    }

    data class TraceAggregationResult(
        val traceId: String,
        val spans: List<SpanMongoEntity>,
        val serviceName: String?,
        val dateNano: Long,
        val durationNano: Long
    )
}