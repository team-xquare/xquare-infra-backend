package xquare.app.xquareinfra.adapter.out.persistence.trace.repository.dto

import xquare.app.xquareinfra.domain.trace.model.Span

data class PersistenceSpanResponse(
    val spans: List<Span>,
    val hasMore: Boolean
)
