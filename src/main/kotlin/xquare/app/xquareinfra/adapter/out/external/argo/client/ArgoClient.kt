package xquare.app.xquareinfra.adapter.out.external.argo.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import xquare.app.xquareinfra.adapter.out.external.argo.dto.ArgoWorkflowListResponse
import xquare.app.xquareinfra.adapter.out.external.argo.dto.ScheduleWorkflowRequest

@FeignClient(name = "argoClient", url = "\${argo.server.url}")
interface ArgoClient {
    
    @GetMapping("/api/v1/workflows/{namespace}")
    fun getWorkflows(
        @PathVariable("namespace") namespace: String,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<ArgoWorkflowListResponse>
    
    @PostMapping("/api/v1/workflows/{namespace}/submit")
    fun submitWorkflow(
        @PathVariable("namespace") namespace: String,
        @RequestHeader("Authorization") token: String,
        request: ScheduleWorkflowRequest
    ): ResponseEntity<Any>
}
