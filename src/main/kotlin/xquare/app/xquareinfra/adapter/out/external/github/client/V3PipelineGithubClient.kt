package xquare.app.xquareinfra.adapter.out.external.github.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import xquare.app.xquareinfra.adapter.out.external.github.client.dto.request.DispatchEventRequest


@FeignClient(name = "v3-github", url = "\${github.url}")
interface V3PipelineGithubClient {
    @PostMapping("/repos/team-xquare/xquare-gitops-repo-v3/dispatches")
    fun dispatchWorkflowGitops(
        @RequestHeader("Authorization") authorization: String?,
        @RequestHeader("Accept") accept: String?,
        @RequestBody request: DispatchEventRequest?
    )
}