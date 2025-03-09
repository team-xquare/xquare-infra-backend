package xquare.app.xquareinfra.application.container.port.out

import xquare.app.xquareinfra.domain.container.model.Language

interface WriteValuesPort {
    fun writeValues(
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
}