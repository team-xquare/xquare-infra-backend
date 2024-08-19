package xquare.app.xquareinfra.adapter.out.persistence.deploy

import org.springframework.stereotype.Component
import xquare.app.xquareinfra.adapter.out.persistence.Mapper
import xquare.app.xquareinfra.adapter.out.persistence.team.TeamMapper
import xquare.app.xquareinfra.domain.deploy.model.Deploy
import xquare.app.xquareinfra.infrastructure.persistence.deploy.DeployJpaEntity

@Component
class DeployMapper(
    private val teamMapper: TeamMapper
) : Mapper<Deploy, DeployJpaEntity> {
    override fun toEntity(domain: Deploy): DeployJpaEntity {
        return DeployJpaEntity(
            id = domain.id,
            deployName = domain.deployName,
            organization = domain.organization,
            repository = domain.repository,
            projectRootDir = domain.projectRootDir,
            oneLineDescription = domain.oneLineDescription,
            teamJpaEntity = teamMapper.toEntity(domain.team),
            secretKey = domain.secretKey,
            deployStatus = domain.deployStatus,
            deployType = domain.deployType,
            useMysql = domain.useMysql,
            useRedis = domain.useRedis,
            isV2 = domain.isV2
        )
    }

    override fun toDomain(entity: DeployJpaEntity): Deploy {
        return Deploy(
            id = entity.id,
            deployName = entity.deployName,
            organization = entity.organization,
            repository = entity.repository,
            projectRootDir = entity.projectRootDir,
            oneLineDescription = entity.oneLineDescription,
            team = teamMapper.toDomain(entity.teamJpaEntity),
            secretKey = entity.secretKey,
            deployStatus = entity.deployStatus,
            deployType = entity.deployType,
            useMysql = entity.useMysql,
            useRedis = entity.useRedis,
            isV2 = entity.isV2
        )
    }
}