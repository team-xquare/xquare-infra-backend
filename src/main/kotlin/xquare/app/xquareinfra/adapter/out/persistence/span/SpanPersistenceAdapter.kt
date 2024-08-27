package xquare.app.xquareinfra.adapter.out.persistence.span

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.span.repository.SpanRepository
import xquare.app.xquareinfra.application.span.port.out.SaveSpanPort
import xquare.app.xquareinfra.domain.span.model.Span

@Component
class SpanPersistenceAdapter(
    private val spanRepository: SpanRepository,
    private val spanMapper: SpanMapper
) : SaveSpanPort{
    override fun save(span: Span): Span {
        return spanMapper.toModel(spanRepository.save(spanMapper.toEntity(span)))
    }
}