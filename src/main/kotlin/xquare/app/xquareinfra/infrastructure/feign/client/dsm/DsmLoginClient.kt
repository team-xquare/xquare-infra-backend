package xquare.app.xquareinfra.infrastructure.feign.client.dsm

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import xquare.app.xquareinfra.infrastructure.feign.client.dsm.dto.GetDsmUserInfoResponse

@FeignClient(name = "dsm-login", url = "https://prod-server.xquare.app/dsm-login")
interface DsmLoginClient {
    @PostMapping("/user/user-data")
    fun getUserInfo(@RequestBody dsmLoginRequest: DsmLoginRequest): GetDsmUserInfoResponse
}

@JsonNaming(SnakeCaseStrategy::class)
data class DsmLoginRequest(
    val accountId: String,
    val password: String
)