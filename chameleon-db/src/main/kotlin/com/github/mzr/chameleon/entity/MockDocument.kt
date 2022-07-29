package com.github.mzr.chameleon.entity

import org.springframework.data.annotation.Id
import org.springframework.data.couchbase.core.index.QueryIndexDirection
import org.springframework.data.couchbase.core.index.QueryIndexed
import org.springframework.data.couchbase.core.mapping.Document
import org.springframework.data.couchbase.core.mapping.Field
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy
import java.time.LocalDateTime

@Document
data class MockDocument(
    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    var id: String? = null,
    @Field
    var createDate: LocalDateTime = LocalDateTime.now(),
    @Field
    var updateDate: LocalDateTime = LocalDateTime.now(),
    @Field
    var active: Boolean,
    @Field
    @QueryIndexed(direction = QueryIndexDirection.ASCENDING)
    var path: String,
    @Field
    @QueryIndexed(direction = QueryIndexDirection.ASCENDING)
    var name: String,
    @Field
    var requests: List<RequestDocument>
)