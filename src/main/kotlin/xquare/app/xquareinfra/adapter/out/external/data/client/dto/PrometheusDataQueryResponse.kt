package xquare.app.xquareinfra.adapter.out.external.data.client.dto

data class PrometheusDataQueryResponse(
    val status: String,
    val data: PrometheusData,
)

data class PrometheusData(
    val resultType: String,
    val result: List<PrometheusResult>,
)

data class PrometheusResult(
    val metric: Map<String, Any>,
    val values: List<Pair<Double, String>>,
)

