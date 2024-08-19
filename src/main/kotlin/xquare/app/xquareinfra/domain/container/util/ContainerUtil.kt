package xquare.app.xquareinfra.domain.container.util

import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity
import xquare.app.xquareinfra.domain.container.model.ContainerEnvironment
import xquare.app.xquareinfra.domain.deploy.domain.Deploy

object ContainerUtil {
    fun generateDomain(containerJpaEntity: ContainerJpaEntity): String {
        if(containerJpaEntity.subDomain!!.isEmpty() || containerJpaEntity.subDomain!! == "null" || containerJpaEntity.subDomain == null) {
            val baseDomain = when (containerJpaEntity.containerEnvironment) {
                ContainerEnvironment.prod -> "prod-server.xquare.app"
                else -> "stag-server.xquare.app"
            }
            return "https://$baseDomain/${containerJpaEntity.deploy.deployName}"
        }
        return containerJpaEntity.subDomain!!
    }

    fun getContainerName(deploy: Deploy, containerJpaEntity: ContainerJpaEntity): String {
        if(deploy.isV2) {
            return "${deploy.deployName}-${containerJpaEntity.containerEnvironment}"
        }
        else return "${deploy.deployName}-${deploy.deployType}-${containerJpaEntity.containerEnvironment}"
    }

    fun getContainerName(deploy: Deploy, containerEnvironment: ContainerEnvironment): String {
        if(deploy.isV2) {
            return "${deploy.deployName}-${containerEnvironment}"
        }
        else return "${deploy.deployName}-${deploy.deployType}-${containerEnvironment}"
    }

    fun getNamespaceName(deploy: Deploy, containerEnvironment: ContainerEnvironment): String {
        return "${deploy.team.teamNameEn}-$containerEnvironment"
    }
}