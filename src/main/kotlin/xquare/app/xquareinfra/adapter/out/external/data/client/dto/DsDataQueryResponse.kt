package xquare.app.xquareinfra.adapter.out.external.data.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class DsDataQueryResponse(
    val results: DsDataResults?,
)

data class DsDataResults(
    @JsonProperty("A")
    val a: DsDataA,
)

data class DsDataA(
    val frames: List<DsDataFrame>,
)

data class DsDataFrame(
    val data: DsData = DsData(emptyList()),
)

data class DsData(
    val values: List<List<Any?>> = emptyList()
)
