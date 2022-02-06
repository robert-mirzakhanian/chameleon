package com.github.crazytosser46.chameleon.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.web.bind.annotation.RequestMethod

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "operation"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = AnyRequestModel::class, name = "ANY"),
    JsonSubTypes.Type(value = IsOneOfRequestModel::class, name = "IS_ONE_OF"),
    JsonSubTypes.Type(value = EqualsRequestModel::class, name = "EQUALS"),
    JsonSubTypes.Type(value = NotEqualsRequestModel::class, name = "NOT_EQUALS")
)
sealed class RequestModel {
    abstract val uri: String
    abstract val method: RequestMethod
    abstract val headers: Map<String, String>
    abstract val response: ResponseModel
}

data class AnyRequestModel(
    override val uri: String,
    override val method: RequestMethod,
    override val headers: Map<String, String> = mapOf(),
    override val response: ResponseModel
) : RequestModel()

data class EqualsRequestModel(
    override val uri: String,
    override val method: RequestMethod,
    override val headers: Map<String, String> = mapOf(),
    override val response: ResponseModel,
    val paramMap: Map<String, String>,
) : RequestModel()

data class IsOneOfRequestModel(
    override val uri: String,
    override val method: RequestMethod,
    override val headers: Map<String, String> = mapOf(),
    override val response: ResponseModel,
    val paramMap: Map<String, List<String>>
) : RequestModel()

data class NotEqualsRequestModel(
    override val uri: String,
    override val method: RequestMethod,
    override val headers: Map<String, String> = mapOf(),
    override val response: ResponseModel,
    val paramMap: Map<String, String>
) : RequestModel()