package xquare.app.xquareinfra.infrastructure.opentelemtry.receiver

import io.grpc.stub.StreamObserver
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceResponse
import io.opentelemetry.proto.collector.trace.v1.TraceServiceGrpc
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.context.ApplicationEventPublisher
import xquare.app.xquareinfra.infrastructure.opentelemtry.event.SpanReceivedEvent

@GrpcService
class TraceReceiver(private val eventPublisher: ApplicationEventPublisher) : TraceServiceGrpc.TraceServiceImplBase() {
    override fun export(request: ExportTraceServiceRequest, responseObserver: StreamObserver<ExportTraceServiceResponse>) {
        request.resourceSpansList.flatMap { it.scopeSpansList }
            .flatMap { it.spansList }
            .forEach { span ->
                eventPublisher.publishEvent(SpanReceivedEvent(this, span))
            }

        val response = ExportTraceServiceResponse.getDefaultInstance()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}