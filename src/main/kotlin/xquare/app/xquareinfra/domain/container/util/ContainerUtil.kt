package xquare.app.xquareinfra.domain.container.util

import xquare.app.xquareinfra.domain.container.model.Container
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.model.Deploy

object ContainerUtil {
    fun generateDomain(container: Container): String {
        if(container.subDomain!!.isEmpty() || container.subDomain!! == "null") {
            val baseDomain = when (container.containerEnvironment) {
                ContainerEnvironment.prod -> "prod-server.xquare.app"
                else -> "stag-server.xquare.app"
            }
            return "https://$baseDomain/${container.deploy.deployName}"
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

    fun getNamespaceName(deploy: Deploy, containerEnvironment: ContainerEnvironment): String {
        return "${deploy.team.teamNameEn}-$containerEnvironment"
    }
}