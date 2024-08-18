package xquare.app.xquareinfra.infrastructure.external.data.client.dto

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
    val data: Data = Data(emptyList()),
)

data class Data(
    val values: List<List<Any?>> = emptyList()
)
