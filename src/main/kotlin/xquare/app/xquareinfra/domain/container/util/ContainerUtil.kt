package xquare.app.xquareinfra.domain.container.util

import xquare.app.xquareinfra.domain.container.domain.Container
import xquare.app.xquareinfra.domain.container.domain.ContainerEnvironment

object ContainerUtil {
    fun generateDomain(container: Container): String {
        if(container.subDomain!!.isEmpty() || container.subDomain!! == "null" || container.subDomain == null) {
            val baseDomain = when (container.containerEnvironment) {
                ContainerEnvironment.prod -> "prod-server.xquare.app"
                else -> "stag-server.xquare.app"
            }
            return "https://$baseDomain/${container.deploy.deployName}"
        }
        return container.subDomain!!
    }
}