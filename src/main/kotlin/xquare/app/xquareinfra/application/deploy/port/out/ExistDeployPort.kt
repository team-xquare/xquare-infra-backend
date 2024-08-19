package xquare.app.xquareinfra.application.deploy.port.out

interface ExistDeployPort {
    fun existByDeployName(deployName: String): Boolean
}