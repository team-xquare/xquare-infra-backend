package xquare.app.xquareinfra.domain.container.util

import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity

object ContainerUtil {
    fun generateDomain(containerJpaEntity: ContainerJpaEntity): String {
        if(containerJpaEntity.subDomain!!.isEmpty() || containerJpaEntity.subDomain!! == "null" || containerJpaEntity.subDomain == null) {
            val baseDomain = when (containerJpaEntity.containerEnvironment) {
                ContainerEnvironment.prod -> "prod-server.xquare.app"
                else -> "stag-server.xquare.app"
            }
            return "https://$baseDomain/${containerJpaEntity.deployJpaEntity.deployName}"
        }
        return containerJpaEntity.subDomain!!
    }

    fun getContainerName(deployJpaEntity: DeployJpaEntity, containerJpaEntity: ContainerJpaEntity): String {
        if(deployJpaEntity.isV2) {
            return "${deployJpaEntity.deployName}-${containerJpaEntity.containerEnvironment}"
        }
        else return "${deployJpaEntity.deployName}-${deployJpaEntity.deployType}-${containerJpaEntity.containerEnvironment}"
    }

    fun getContainerName(deployJpaEntity: DeployJpaEntity, containerEnvironment: ContainerEnvironment): String {
        if(deployJpaEntity.isV2) {
            return "${deployJpaEntity.deployName}-${containerEnvironment}"
        }
        else return "${deployJpaEntity.deployName}-${deployJpaEntity.deployType}-${containerEnvironment}"
    }

    fun getNamespaceName(deployJpaEntity: DeployJpaEntity, containerEnvironment: ContainerEnvironment): String {
        return "${deployJpaEntity.teamJpaEntity.teamNameEn}-$containerEnvironment"
    }
}