package dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class VkRecordSearchResult(
    val count: Long,
    @JsonProperty(value = "total_count") val totalCount: Long,
    @JsonProperty(value = "next_from") val nextFrom: String?,
    val items: List<VkRecord>
)