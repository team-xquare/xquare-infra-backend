package xquare.app.xquareinfra.adapter.out.external.argo.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import xquare.app.xquareinfra.adapter.out.external.argo.dto.ArgoAuthRequest
import xquare.app.xquareinfra.adapter.out.external.argo.dto.ArgoAuthResponse

@FeignClient(name = "argoAuthClient", url = "\${argo.server.url}")
interface ArgoAuthClient {
    @PostMapping("/api/v1/session")
    fun login(@RequestBody request: ArgoAuthRequest): ResponseEntity<ArgoAuthResponse>
}
