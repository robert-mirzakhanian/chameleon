package com.github.crazytosser46.chameleon.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "matcherOperation"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = AnyRequestModel::class, name = "ANY"),
    JsonSubTypes.Type(value = IsOneOfRequestModel::class, name = "IS_ONE_OF"),
    JsonSubTypes.Type(value = EqualsRequestModel::class, name = "EQUALS"),
    JsonSubTypes.Type(value = NotEqualsRequestModel::class, name = "NOT_EQUALS")
)
sealed class RequestModel(val operation: MatcherOperation) {
    abstract var url: String
    abstract val headers: Map<String, String>
    abstract val response: ResponseModel
}

data class AnyRequestModel(
    override var url: String,
    override val headers: Map<String, String> = mapOf(),
    override val response: ResponseModel
) : RequestModel(MatcherOperation.ANY)

data class EqualsRequestModel(
    override var url: String,
    override val headers: Map<String, String> = mapOf(),
    override val response: ResponseModel,
    val paramPath: String,
    val paramValue: String
) : RequestModel(MatcherOperation.EQUALS)

data class IsOneOfRequestModel(
    override var url: String,
    override val headers: Map<String, String> = mapOf(),
    override val response: ResponseModel,
    val paramPath: String,
    val paramValues: List<String>
) : RequestModel(MatcherOperation.IS_ONE_OF)

data class NotEqualsRequestModel(
    override var url: String,
    override val headers: Map<String, String> = mapOf(),
    override val response: ResponseModel
) : RequestModel(MatcherOperation.NOT_EQUALS)