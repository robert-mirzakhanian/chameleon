package com.github.crazytosser46.chameleon.repository

import com.github.crazytosser46.chameleon.entity.MockDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface MockRepository: ReactiveMongoRepository<MockDocument, ObjectId> {
    suspend fun findFirstByUri(uri: String): MockDocument?
}