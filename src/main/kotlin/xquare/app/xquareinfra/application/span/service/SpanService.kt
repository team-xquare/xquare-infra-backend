package xquare.app.xquareinfra.application.span.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.adapter.`in`.span.dto.response.GetRootSpanListResponse
import xquare.app.xquareinfra.adapter.`in`.span.dto.response.RootSpan
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.span.port.`in`.SpanUseCase
import xquare.app.xquareinfra.application.span.port.out.FindSpanPort
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.util.TimeUtil
import java.util.*

@Service
class SpanService(
    private val findDeployPort: FindDeployPort,
    private val findSpanPort: FindSpanPort
) : SpanUseCase {
    override fun getRootSpanByDeployIdAndEnvironment(
        deployId: UUID,
        environment: ContainerEnvironment,
        timeRangeMinute: Long
    ): GetRootSpanListResponse {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val serviceName = ContainerUtil.getContainerName(deploy, environment)

        val timeRangeInNanos = TimeUtil.getTimeRangeInNanos(timeRangeMinute)

        val rootSpanList = findSpanPort.findRootSpanListByServiceNameInTimeRange(
            serviceName = serviceName,
            startTimeUnixNano = timeRangeInNanos.past,
            endTimeUnixNano = timeRangeInNanos.now
        )

        val rootSpanDtoList = rootSpanList.map {
            RootSpan(
                date = TimeUtil.unixNanoToKoreanTime(it.startTimeUnixNano),
                resource = it.name,
                durationMs = TimeUtil.unixNanoToMilliseconds(it.endTimeUnixNano - it.startTimeUnixNano),
                method = it.getAttributeValue("http.method")?.stringValue,
                statusCode = it.getAttributeValue("http.status_code")?.intValue
            )
        }

        return GetRootSpanListResponse(rootSpanDtoList)
    }
}