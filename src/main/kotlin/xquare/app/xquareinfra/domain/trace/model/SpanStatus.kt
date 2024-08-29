package xquare.app.xquareinfra.domain.trace.model

import org.springframework.data.mongodb.core.mapping.Field

data class SpanStatus(
    @Field("code")
    val code: Int,

    @Field("description")
    val description: String?
)