package xquare.app.xquareinfra.adapter.out.persistence.container

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.Mapper
import xquare.app.xquareinfra.adapter.out.persistence.deploy.DeployMapper
import xquare.app.xquareinfra.domain.container.model.Container
import xquare.app.xquareinfra.infrastructure.persistence.container.ContainerJpaEntity

@Component
class ContainerMapper(
    private val deployMapper: DeployMapper
) : Mapper<Container, ContainerJpaEntity> {
    override fun toEntity(domain: Container): ContainerJpaEntity {
        return ContainerJpaEntity(
            id = domain.id,
            deployJpaEntity = deployMapper.toEntity(domain.deploy),
            containerEnvironment = domain.containerEnvironment,
            lastDeploy = domain.lastDeploy,
            subDomain = domain.subDomain,
            environmentVariable = domain.environmentVariable,
            githubBranch = domain.githubBranch,
            containerPort = domain.containerPort,
            webhookInfo = domain.webhookInfo
        )
    }

    override fun toDomain(entity: ContainerJpaEntity): Container {
        return Container(
            id = entity.id,
            deploy = deployMapper.toDomain(entity.deployJpaEntity),
            containerEnvironment = entity.containerEnvironment,
            lastDeploy = entity.lastDeploy,
            subDomain = entity.subDomain,
            environmentVariable = entity.environmentVariable,
            githubBranch = entity.githubBranch,
            containerPort = entity.containerPort,
            webhookInfo = entity.webhookInfo
        )
    }
}