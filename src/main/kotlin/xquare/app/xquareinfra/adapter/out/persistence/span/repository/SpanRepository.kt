package xquare.app.xquareinfra.adapter.out.persistence.span.repository

import org.springframework.data.mongodb.repository.MongoRepository
import xquare.app.xquareinfra.infrastructure.persistence.span.SpanMongoEntity

interface SpanRepository : MongoRepository<SpanMongoEntity, String> {
}