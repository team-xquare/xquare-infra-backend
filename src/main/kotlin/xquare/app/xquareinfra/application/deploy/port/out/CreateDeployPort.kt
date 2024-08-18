package xquare.app.xquareinfra.application.deploy.port.out

import xquare.app.xquareinfra.domain.deploy.domain.Deploy

interface CreateDeployPort {
    fun createDeploy(deploy: Deploy): Deploy
}