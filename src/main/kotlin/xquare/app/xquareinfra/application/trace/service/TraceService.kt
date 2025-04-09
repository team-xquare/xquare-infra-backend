package xquare.app.xquareinfra.application.trace.service

import org.springframework.stereotype.Service
import xquare.app.xquareinfra.application.deploy.port.out.FindDeployPort
import xquare.app.xquareinfra.application.deploy.port.out.SaveDeployPort
import xquare.app.xquareinfra.application.trace.port.`in`.TraceUseCase
import xquare.app.xquareinfra.application.trace.port.out.DatadogPort
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.container.util.ContainerUtil
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import java.util.*

@Service
class TraceService(
    private val findDeployPort: FindDeployPort,
    private val datadogPort: DatadogPort,
    private val saveDeployPort: SaveDeployPort
) : TraceUseCase {
    override fun getServiceEmbedDashboard(deployId: UUID, environment: ContainerEnvironment): String {
        val deploy = findDeployPort.findById(deployId) ?: throw BusinessLogicException.DEPLOY_NOT_FOUND
        val serviceName = ContainerUtil.getContainerName(deploy, environment)
        val dashboardList = datadogPort.getAllDashboard()

        val dashboard = dashboardList.dashboards.find {
            it.title.startsWith(serviceName)
        } ?: throw BusinessLogicException.DASHBOARD_NOT_FOUND

        val dashboardToken = deploy.dashboardToken ?: run {
            val sharedDashboard = datadogPort.createSharedDashboard(dashboard).sharedDashboardResponse
            saveDeployPort.saveDeploy(deploy.updateDeployToken(sharedDashboard.token))

            return sharedDashboard.publicUrl
        }

        return datadogPort.getSharedDashboard(token = dashboardToken).sharedDashboardResponse.publicUrl
    }
}