package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.domain.container.model.Language

interface WriteValuesPort {
    fun v1WriteValues(
        club: String,
        name: String,
        organization: String,
        repository: String,
        branch: String,
        environment: String,
        containerPort: Int,
        domain: String,
        language: Language,
        criticalService: Boolean
    )

    fun v2WriteValues(
        club: String,
        name: String,
        organization: String,
        repository: String,
        branch: String,
        environment: String,
        containerPort: Int,
        domain: String,
        language: Language,
        criticalService: Boolean,
        buildConfig: String,
        appInstallId: String
    )
}