package com.github.mzr.chameleon.repository

import com.github.mzr.chameleon.entity.MockDocument
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface MockRepository : ReactiveCouchbaseRepository<MockDocument, String> {
    fun findAllByPath(path: String): Flux<MockDocument>

    fun findFirstById(id: String): Mono<MockDocument?>

    fun findFirstByName(name: String): Flux<MockDocument?>

    fun findFirstByPath(path: String): Mono<MockDocument?>

    fun deleteByName(name: String): Mono<Void>
}