package xquare.app.xquareinfra.infrastructure.vault.config

import com.bettercloud.vault.SslConfig
import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import xquare.app.xquareinfra.infrastructure.vault.env.VaultProperty

@Configuration
class VaultConfig(
    private val vaultProperty: VaultProperty
) {
    @Bean
    fun vault() = Vault(
        VaultConfig()
            .address(vaultProperty.address)
            .token(vaultProperty.vaultToken)
            .sslConfig(SslConfig().verify(false).build())
            .engineVersion(1)
            .build()
    )
}