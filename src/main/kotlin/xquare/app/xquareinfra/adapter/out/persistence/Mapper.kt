package xquare.app.xquareinfra.adapter.out.persistence

interface Mapper<D, E> {
    fun toEntity(domain: D): E
    fun toDomain(entity: E): D
    fun toEntities(domains: List<D>): List<E> = domains.map(::toEntity)
    fun toDomains(entities: List<E>): List<D> = entities.map(::toDomain)
}