package xquare.app.xquareinfra.adapter.out.persistence.trace.repository

import org.springframework.data.repository.CrudRepository
import xquare.app.xquareinfra.infrastructure.listener.TraceEventCache

interface TraceCacheRepository : CrudRepository<TraceEventCache> {
}