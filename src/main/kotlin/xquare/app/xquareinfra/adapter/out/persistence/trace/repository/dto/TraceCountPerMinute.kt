package xquare.app.xquareinfra.adapter.out.persistence.trace.repository.dto

import java.time.Instant

data class TraceCountPerMinute(
    val _id: Instant,
    val count: Int
)