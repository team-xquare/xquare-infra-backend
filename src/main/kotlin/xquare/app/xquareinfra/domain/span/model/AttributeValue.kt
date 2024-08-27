package xquare.app.xquareinfra.domain.span.model

data class AttributeValue(
    val stringValue: String? = null,
    val intValue: Long? = null,
    val doubleValue: Double? = null,
    val boolValue: Boolean? = null,
    val arrayValue: List<AttributeValue>? = null
)