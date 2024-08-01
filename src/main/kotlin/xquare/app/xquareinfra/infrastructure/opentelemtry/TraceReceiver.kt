package xquare.app.xquareinfra.infrastructure.opentelemtry

import io.grpc.stub.StreamObserver
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceResponse
import io.opentelemetry.proto.collector.trace.v1.TraceServiceGrpc
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class TraceReceiver : TraceServiceGrpc.TraceServiceImplBase() {
    override fun export(request: ExportTraceServiceRequest, responseObserver: StreamObserver<ExportTraceServiceResponse>) {
        println("Received ${request.resourceSpansList.size} resource spans")

        for (resourceSpans in request.resourceSpansList) {
            for (scopeSpans in resourceSpans.scopeSpansList) {
                for (span in scopeSpans.spansList) {
                    println("Received span: ${span.name}")
                }
            }
        }

        val response = ExportTraceServiceResponse.getDefaultInstance()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}