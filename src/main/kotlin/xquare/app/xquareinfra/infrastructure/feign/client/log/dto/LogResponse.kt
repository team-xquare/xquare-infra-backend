package xquare.app.xquareinfra.infrastructure.feign.client.log.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class LogResponse(
    val results: Results,
)

data class Results(
    @JsonProperty("A")
    val a: A,
)

data class A(
    val frames: List<Frame>,
)

data class Frame(
    val data: Data,
)

data class Data(
    val values: List<List<Any?>>,
)
