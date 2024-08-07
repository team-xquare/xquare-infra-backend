package xquare.app.xquareinfra.infrastructure.external.client.github

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import xquare.app.xquareinfra.infrastructure.external.client.github.dto.request.DispatchEventRequest


@FeignClient(name = "github", url = "\${github.url}")
interface GithubClient {
    @PostMapping("/repos/team-xquare/xquare-gitops-repo/dispatches")
    fun dispatchWorkflow(
        @RequestHeader("Authorization") authorization: String?,
        @RequestHeader("Accept") accept: String?,
        @RequestBody request: DispatchEventRequest?
    )
}