package xquare.app.xquareinfra.infrastructure.external.feign.client.data.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class DataQueryResponse(
    val results: Results?,
)

data class Results(
    @JsonProperty("A")
    val a: A,
)

data class A(
    val frames: List<Frame>,
)

data class Frame(
    val data: Data?,
)

data class Data(
    val values: List<List<Any?>>,
)
