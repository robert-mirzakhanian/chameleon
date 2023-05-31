package com.github.mzr.chameleon.repository

import com.github.mzr.chameleon.entity.MockDocument
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface MockRepository : ReactiveCouchbaseRepository<MockDocument, String> {
    fun findAllByPath(path: String): Flux<MockDocument>

    fun findAllByPath(path: String, pageable: Pageable): Flux<MockDocument>

    fun findFirstById(id: String): Mono<MockDocument?>

    fun findFirstByName(name: String): Flux<MockDocument?>
    fun findAllByName(name: String, pageable: Pageable): Flux<MockDocument?>

    fun findFirstByPath(path: String): Mono<MockDocument?>

    fun deleteByName(name: String): Mono<Void>

    fun findAllBy(pageable: Pageable): Flux<MockDocument>
}