package com.github.crazytosser46.chameleon.repository

import com.github.crazytosser46.chameleon.entity.MockDocument
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface MockRepository: ReactiveMongoRepository<MockDocument, String> {
    fun findAllByPath(path: String): Flux<MockDocument>

    fun findFirstById(id: String): Mono<MockDocument?>

    fun findFirstByName(name: String): Flux<MockDocument?>

    fun deleteByName(name: String): Mono<Void>
}