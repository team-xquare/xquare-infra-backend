package xquare.app.xquareinfra.application.deploy.port.out

import xquare.app.xquareinfra.domain.deploy.model.Deploy

interface SaveDeployPort {
    fun saveDeploy(deploy: Deploy): Deploy
    fun deleteDeploy(deploy: Deploy)
}