package xquare.app.xquareinfra.infrastructure.external.resttemplate

import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class RestTemplateHttpRequestSender(
    private val restTemplate: RestTemplate
) {
    fun requestPost(url: String, body: Any): String? {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val request = HttpEntity(body, headers)
        return restTemplate.postForObject(url, request, String::class.java)
    }
}