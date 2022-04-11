package com.github.crazytosser46.chameleon.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("Mock")
data class MockDocument(
    @Id
    var id: String? = null,
    var createDate: LocalDateTime = LocalDateTime.now(),
    var updateDate: LocalDateTime = LocalDateTime.now(),
    var active: Boolean,
    @Indexed
    var path: String,
    @Indexed(unique = true)
    var name: String,
    var requests: List<RequestDocument>
)