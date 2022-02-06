package com.github.crazytosser46.chameleon.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("Mock")
data class MockDocument(
    @Id
    var id: ObjectId? = null,
    var createDate: LocalDateTime = LocalDateTime.now(),
    var updateDate: LocalDateTime = LocalDateTime.now(),
    val isActive: Boolean,
    @Indexed
    var url: String,
    var name: String,
    var requests: List<RequestDocument> = emptyList()
)