package xquare.app.xquareinfra.application.trace.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import xquare.app.xquareinfra.adapter.`in`.trace.dto.response.ServiceMapResponse
import xquare.app.xquareinfra.application.trace.interaction.InteractionHandlerFactory
import xquare.app.xquareinfra.domain.trace.model.Span
import xquare.app.xquareinfra.domain.trace.model.SpanStatus

class ServiceMapBuilderTest {

    @Test
    fun `test service map building with enum status`() {
        val spans = listOf(
            Span(
                traceId = "trace1",
                spanId = "span1",
                name = "HTTP PATCH",
                startTimeUnixNano = 1000000,
                endTimeUnixNano = 2000000,
                attributes = mapOf(
                    "http_method" to "PATCH",
                    "http_status_code" to 200,
                    "net_peer_name" to "mysql:3306"
                ),
                events = emptyList(),
                parentSpanId = null,
                serviceName = "service-A",
                id = "",
                kind = 1,
                status = SpanStatus(
                    code = 1,
                    description = ""
                )
            ),
            Span(
                traceId = "trace1",
                spanId = "span2",
                name = "DB SELECT",
                startTimeUnixNano = 1500000,
                endTimeUnixNano = 2500000,
                attributes = mapOf(
                    "db_system" to "mysql",
                    "db_operation" to "SELECT"
                ),
                events = emptyList(),
                parentSpanId = "span1",
                serviceName = "service-B",
                id = "",
                kind = 1,
                status = SpanStatus(
                    code = 1,
                    description = ""
                )
            )
        )

        val handlerFactory = InteractionHandlerFactory()
        val serviceMapBuilder = ServiceMapBuilder(handlerFactory)
        val serviceMapResponse: ServiceMapResponse = serviceMapBuilder.toServiceMapResponse(spans)

        assertEquals("success", serviceMapResponse.status)
        assertEquals(1, serviceMapResponse.data.metrics.size)

        val metric = serviceMapResponse.data.metrics[0]
        assertEquals(2, metric.nodes.size)
        assertEquals(1, metric.edges.size)

        val nodeA = metric.nodes.find { it.nodeId == "service-A" }
        val nodeB = metric.nodes.find { it.nodeId == "service-B" }

        assertNotNull(nodeA)
        assertNotNull(nodeB)
        assertEquals(1, nodeA?.calls)
        assertEquals(1, nodeA?.successes)
        assertEquals(0, nodeA?.failures)
        assertEquals(1, nodeB?.calls)
        assertEquals(1, nodeB?.successes)
        assertEquals(0, nodeB?.failures)

        val edge = metric.edges.find { it.source == "service-A" && it.target == "service-B" }
        assertNotNull(edge)
        assertEquals(1, edge?.calls)
        assertEquals(1, edge?.successes)
        assertEquals(0, edge?.failures)
        assertEquals(1.0, edge?.latencyAvgMs)
    }
}