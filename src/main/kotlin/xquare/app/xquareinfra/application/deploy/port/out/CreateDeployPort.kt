package xquare.app.xquareinfra.application.deploy.port.out

import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity

interface CreateDeployPort {
    fun createDeploy(deployJpaEntity: DeployJpaEntity): DeployJpaEntity
}