package xquare.app.xquareinfra.adapter.out.persistence.trace.repository

import org.springframework.data.mongodb.repository.MongoRepository
import xquare.app.xquareinfra.infrastructure.persistence.trace.TraceMongoEntity

interface TraceRepository : MongoRepository<TraceMongoEntity, String> {
}