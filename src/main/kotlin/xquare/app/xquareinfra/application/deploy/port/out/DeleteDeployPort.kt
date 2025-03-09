package xquare.app.xquareinfra.application.deploy.port.out

interface DeleteDeployPort {
    fun deleteDeploy(
        club: String,
        serviceName: String
    )
}