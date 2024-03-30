package xquare.app.xquareinfra.infrastructure.feign.client.dsm

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import xquare.app.xquareinfra.infrastructure.feign.client.dsm.dto.GetDsmUserInfoResponse

@FeignClient(name = "dsm-login", url = "https://prod-server.xquare.app/dsm-login")
interface DsmLoginClient {
    @GetMapping("/user/user-data")
    fun getUserInfo(@RequestParam("account_id") accountId: String, @RequestParam("password") password: String): feign.Response
}