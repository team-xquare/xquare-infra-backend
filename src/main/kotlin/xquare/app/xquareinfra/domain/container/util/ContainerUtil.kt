package xquare.app.xquareinfra.domain.container.util

import xquare.app.xquareinfra.domain.container.model.Container
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.model.Deploy
import xquare.app.xquareinfra.domain.team.model.Team

object ContainerUtil {
    fun generateDomain(container: Container, deploy: Deploy): String {
        if(container.subDomain!!.isEmpty() || container.subDomain!! == "null") {
            val baseDomain = when (container.containerEnvironment) {
                ContainerEnvironment.prod -> "prod-server.xquare.app"
                else -> "stag-server.xquare.app"
            }
            return "https://$baseDomain/${deploy.deployName}"
        }
        return container.subDomain!!
    }

    fun getContainerName(deploy: Deploy, container: Container): String {
        if(deploy.v2) {
            return "${deploy.deployName}-${container.containerEnvironment}"
        }
        else return "${deploy.deployName}-${deploy.deployType}-${container.containerEnvironment}"
    }

    fun getContainerName(deploy: Deploy, containerEnvironment: ContainerEnvironment): String {
        if(deploy.v2) {
            return "${deploy.deployName}-${containerEnvironment}"
        }
        else return "${deploy.deployName}-${deploy.deployType}-${containerEnvironment}"
    }

    fun getNamespaceName(team: Team, containerEnvironment: ContainerEnvironment): String {
        return "${team.teamNameEn}-$containerEnvironment"
    }

    fun getContainerInfoByFullName(fullName: String): ContainerInfo {
        var serviceName = fullName
        val containerEnvironment = when {
            serviceName.contains("-prod") -> {
                serviceName = serviceName.replace("-prod", "")
                ContainerEnvironment.prod
            }
            serviceName.contains("-stag") -> {
                serviceName = serviceName.replace("-stag", "")
                ContainerEnvironment.stag
            }
            else -> throw IllegalArgumentException("Unknown environment in service name")
        }

        serviceName = serviceName.replace("-be", "").replace("-fe", "")

        return ContainerInfo(serviceName = serviceName, containerEnvironment = containerEnvironment)
    }

}