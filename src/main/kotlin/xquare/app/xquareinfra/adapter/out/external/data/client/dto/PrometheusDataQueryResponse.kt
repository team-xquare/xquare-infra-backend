package xquare.app.xquareinfra.adapter.out.external.data.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PrometheusDataQueryResponse(
    @JsonProperty("status")
    val status: String,
    @JsonProperty("data")
    val data: PrometheusData,
)

data class PrometheusData(
    @JsonProperty("resultType")
    val resultType: String,
    @JsonProperty("result")
    val result: List<PrometheusResult>,
)

data class PrometheusResult(
    @JsonProperty("metric")
    val metric: Map<String, Any>,
    @JsonProperty("values")
    val values: List<List<JsonElement>>,
)

data class JsonElement(
    @JsonProperty("value")
    val value: Any
)