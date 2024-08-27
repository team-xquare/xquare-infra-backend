package xquare.app.xquareinfra.adapter.out.persistence.trace

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.trace.repository.SpanRepository
import xquare.app.xquareinfra.application.trace.port.out.FindSpanPort
import xquare.app.xquareinfra.application.trace.port.out.SaveSpanPort
import xquare.app.xquareinfra.domain.trace.model.Span

@Component
class SpanPersistenceAdapter(
    private val spanRepository: SpanRepository,
    private val spanMapper: SpanMapper
) : SaveSpanPort, FindSpanPort {
    override fun save(span: Span): Span {
        return spanMapper.toModel(spanRepository.save(spanMapper.toEntity(span)))
    }

    override fun saveAll(spans: List<Span>) {
        spanRepository.saveAll(spans.map { spanMapper.toEntity(it) })
    }

    override fun findRootSpanListByServiceNameInTimeRange(
        serviceName: String,
        startTimeUnixNano: Long,
        endTimeUnixNano: Long
    ): List<Span> {
        return spanRepository.findSpansBetweenTimesByRootServiceName(
            startTimeUnixNano,
            endTimeUnixNano,
            serviceName
        ).map { spanMapper.toModel(it) }
    }
}