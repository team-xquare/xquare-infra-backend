package xquare.app.xquareinfra.application.deploy.port.out

import xquare.app.xquareinfra.domain.deploy.domain.Deploy

interface saveDeployPort {
    fun saveDeploy(deploy: Deploy): Deploy
}