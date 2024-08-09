package xquare.app.xquareinfra.infrastructure.external.client.dsm

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import xquare.app.xquareinfra.infrastructure.external.feign.client.dsm.dto.GetDsmUserInfoResponse

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