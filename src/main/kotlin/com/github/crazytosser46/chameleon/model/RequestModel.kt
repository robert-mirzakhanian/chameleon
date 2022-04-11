package com.github.crazytosser46.chameleon.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.http.HttpMethod

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
    abstract val method: HttpMethod
    abstract val headers: Map<String, String>?
    abstract val response: ResponseModel
}

data class AnyRequestModel(
    override val method: HttpMethod,
    override val headers: Map<String, String>? = null,
    override val response: ResponseModel
) : RequestModel()

data class EqualsRequestModel(
    override val method: HttpMethod,
    override val headers: Map<String, String>? = null,
    override val response: ResponseModel,
    val paramMap: Map<String, String>,
) : RequestModel()

data class IsOneOfRequestModel(
    override val method: HttpMethod,
    override val headers: Map<String, String>? = null,
    override val response: ResponseModel,
    val paramMap: Map<String, List<String>>
) : RequestModel()

data class NotEqualsRequestModel(
    override val method: HttpMethod,
    override val headers: Map<String, String>? = null,
    override val response: ResponseModel,
    val paramMap: Map<String, String>
) : RequestModel()