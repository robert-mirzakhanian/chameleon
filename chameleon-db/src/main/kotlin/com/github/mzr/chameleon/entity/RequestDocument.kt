package com.github.mzr.chameleon.entity

import com.github.mzr.chameleon.model.MatcherOperation
import org.springframework.data.annotation.Transient
import org.springframework.http.HttpMethod
import java.time.LocalDateTime

sealed class RequestDocument(
    var createDate: LocalDateTime = LocalDateTime.now(),
    var updateDate: LocalDateTime = LocalDateTime.now(),
    @Transient
    val matcherOperation: MatcherOperation
) {
    abstract val isActive: Boolean
    abstract val method: HttpMethod
    abstract val responseDocument: ResponseDocument
    abstract val headers: Map<String, String>?
}

data class AnyRequestDocument(
    override val isActive: Boolean,
    override val method: HttpMethod,
    override val responseDocument: ResponseDocument,
    override val headers: Map<String, String>?
) : RequestDocument(matcherOperation = MatcherOperation.ANY)

data class EqualsRequestDocument(
    override val isActive: Boolean,
    override val method: HttpMethod,
    override val responseDocument: ResponseDocument,
    override val headers: Map<String, String>?,
    val expectedParams: Map<String, String>
) : RequestDocument(matcherOperation = MatcherOperation.EQUALS)

data class IsOneOfRequestDocument(
    override val isActive: Boolean,
    override val method: HttpMethod,
    override val responseDocument: ResponseDocument,
    override val headers: Map<String, String>?,
    val expectedParams: Map<String, List<String>>
) : RequestDocument(matcherOperation = MatcherOperation.IS_ONE_OF)

data class NotEqualsRequestDocument(
    override val isActive: Boolean,
    override val method: HttpMethod,
    override val responseDocument: ResponseDocument,
    override val headers: Map<String, String>?,
    val expectedParams: Map<String, String>
) : RequestDocument(matcherOperation = MatcherOperation.NOT_EQUALS)