package xquare.app.xquareinfra.domain.deploy.application.port.out

interface ExistDeployPort {
    fun existByDeployName(deployName: String): Boolean
}