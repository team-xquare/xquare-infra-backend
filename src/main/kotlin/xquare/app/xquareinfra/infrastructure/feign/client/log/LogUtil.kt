package xquare.app.xquareinfra.infrastructure.feign.client.log

import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.domain.DeployType

object LogUtil {
    fun extractOrganization(githubUrl: String): String {
        val parts = githubUrl.split("/")
        return if (parts.size >= 4) {
            parts[3]
        } else {
            ""
        }
    }

    fun getRepository(githubUrl: String): String {
        val parts = githubUrl.split("/")
        return if (parts.size >= 4) {
            "${parts[3]}/${parts[4]}"
        } else {
            ""
        }
    }

    fun makeLogQuery(team: String, containerName: String, serviceType: DeployType, envType: ContainerEnvironment): String {
        val fullName = "${containerName}-${serviceType.toString().lowercase()}-${envType.toString().lowercase()}"
        return "{job=\"$team-${envType.toString().lowercase()}/$fullName\", container=~\"$fullName\", stream=~\"stdout\"} |~ \"(?i)\" \n"
    }
}