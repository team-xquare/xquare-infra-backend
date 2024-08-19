package xquare.app.xquareinfra.application.deploy.port.out

import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity

interface SaveDeployPort {
    fun saveDeploy(deployJpaEntity: DeployJpaEntity): DeployJpaEntity
}