package xquare.app.xquareinfra.domain.deploy.application.port.out

import xquare.app.xquareinfra.domain.deploy.domain.Deploy

interface FindDeployPort {
    fun findByDeployName(deployName: String): Deploy?
}