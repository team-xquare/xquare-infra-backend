package xquare.app.xquareinfra.infrastructure.feign.client.cloudflare.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ListDnsRecordsResponse(
    val result: List<Result>,
    val success: Boolean,
    val errors: List<Any?>,
    val messages: List<Any?>,
    @JsonProperty("result_info")
    val resultInfo: ResultInfo,
)

data class Result(
    val id: String,
    @JsonProperty("zone_id")
    val zoneId: String,
    @JsonProperty("zone_name")
    val zoneName: String,
    val name: String,
    val type: String,
    val content: String,
    val proxiable: Boolean,
    val proxied: Boolean,
    val ttl: Long,
    val locked: Boolean,
    val meta: Meta,
    val comment: Any?,
    val tags: List<Any?>,
    @JsonProperty("created_on")
    val createdOn: String,
    @JsonProperty("modified_on")
    val modifiedOn: String,
    val priority: Long?,
)

data class Meta(
    @JsonProperty("auto_added")
    val autoAdded: Boolean,
    @JsonProperty("managed_by_apps")
    val managedByApps: Boolean,
    @JsonProperty("managed_by_argo_tunnel")
    val managedByArgoTunnel: Boolean,
    @JsonProperty("email_routing")
    val emailRouting: Boolean?,
    @JsonProperty("read_only")
    val readOnly: Boolean?,
)

data class ResultInfo(
    val page: Long,
    @JsonProperty("per_page")
    val perPage: Long,
    val count: Long,
    @JsonProperty("total_count")
    val totalCount: Long,
    @JsonProperty("total_pages")
    val totalPages: Long,
)
