package xquare.app.xquareinfra.infrastructure.config.http

import org.apache.http.HeaderElement
import org.apache.http.HeaderElementIterator
import org.apache.http.HttpHost
import org.apache.http.HttpResponse
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.conn.ConnectionKeepAliveStrategy
import org.apache.http.message.BasicHeaderElementIterator
import org.apache.http.protocol.HTTP
import org.apache.http.protocol.HttpContext
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import xquare.app.xquareinfra.infrastructure.env.http.UrlProperties

@Configuration
class HttpKeepAliveStrategyConfiguration(
    private val urlProperties: UrlProperties
) : ConnectionKeepAliveStrategy {

    override fun getKeepAliveDuration(response: HttpResponse, context: HttpContext): Long {
        val it: HeaderElementIterator = BasicHeaderElementIterator(
            response.headerIterator(HTTP.CONN_KEEP_ALIVE)
        )
        while (it.hasNext()) {
            val he: HeaderElement = it.nextElement()
            val param: String = he.name
            val value: String? = he.value
            if (value != null && param.equals("timeout", ignoreCase = true)) {
                return try {
                    value.toLong() * 1000
                } catch (ignore: NumberFormatException) {
                    continue
                }
            }
        }

        val target = context.getAttribute(HttpClientContext.HTTP_TARGET_HOST) as HttpHost
        return when (target.toURI()) {
            urlProperties.deploy -> 60 * 1000L  // 1분
            urlProperties.log -> 30 * 1000L     // 30초
            urlProperties.gocd -> 45 * 1000L    // 45초
            urlProperties.cloudflare -> 90 * 1000L  // 1분 30초
            else -> 30 * 1000L  // 기본값 30초
        }
    }
}