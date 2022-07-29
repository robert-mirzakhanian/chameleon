package com.github.mzr.chameleon.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.http.HttpMethod

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "operation"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = AnyRequestDto::class, name = "ANY"),
    JsonSubTypes.Type(value = IsOneOfRequestDto::class, name = "IS_ONE_OF"),
    JsonSubTypes.Type(value = EqualsRequestDto::class, name = "EQUALS"),
    JsonSubTypes.Type(value = NotEqualsRequestDto::class, name = "NOT_EQUALS")
)
sealed class RequestDto {
    abstract val method: HttpMethod
    abstract val headers: Map<String, String>?
    abstract val response: ResponseDto
}

data class AnyRequestDto(
    override val method: HttpMethod,
    override val headers: Map<String, String>? = null,
    override val response: ResponseDto
) : RequestDto()

data class EqualsRequestDto(
    override val method: HttpMethod,
    override val headers: Map<String, String>? = null,
    override val response: ResponseDto,
    val paramMap: Map<String, String>,
) : RequestDto()

data class IsOneOfRequestDto(
    override val method: HttpMethod,
    override val headers: Map<String, String>? = null,
    override val response: ResponseDto,
    val paramMap: Map<String, List<String>>
) : RequestDto()

data class NotEqualsRequestDto(
    override val method: HttpMethod,
    override val headers: Map<String, String>? = null,
    override val response: ResponseDto,
    val paramMap: Map<String, String>
) : RequestDto()