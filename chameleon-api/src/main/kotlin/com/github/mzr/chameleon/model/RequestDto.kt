package com.github.mzr.chameleon.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpMethod

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = AnyRequestDto::class, name = "ANY"),
    JsonSubTypes.Type(value = EqualsRequestDto::class, name = "EQUALS"),
    JsonSubTypes.Type(value = IsOneOfRequestDto::class, name = "IS_ONE_OF"),
    JsonSubTypes.Type(value = NotEqualsRequestDto::class, name = "NOT_EQUALS")
)
@Schema(
    discriminatorProperty = "type",
    oneOf = [AnyRequestDto::class, EqualsRequestDto::class, IsOneOfRequestDto::class, NotEqualsRequestDto::class],
    discriminatorMapping = [
        DiscriminatorMapping(value = "ANY", schema = AnyRequestDto::class),
        DiscriminatorMapping(value = "EQUALS", schema = EqualsRequestDto::class),
        DiscriminatorMapping(value = "IS_ONE_OF", schema = IsOneOfRequestDto::class),
        DiscriminatorMapping(value = "NOT_EQUALS", schema = NotEqualsRequestDto::class)
    ]
)
sealed class RequestDto {
    @get:Schema(description = "Http method")
    abstract val method: HttpMethod

    @get:Schema(description = "Http headers")
    abstract val headers: Map<String, String>?

    @get:Schema(description = "Expected response")
    abstract val response: ResponseDto
}
data class AnyRequestDto(
    @get:Schema(description = "Http method", required = true)
    override val method: HttpMethod,
    @get:Schema(description = "Http headers", required = false)
    override val headers: Map<String, String>? = null,
    @get:Schema(description = "Expected response", required = true)
    override val response: ResponseDto
) : RequestDto()

data class EqualsRequestDto(
    @get:Schema(description = "Http method")
    override val method: HttpMethod,
    @get:Schema(description = "Http headers", required = false)
    override val headers: Map<String, String>? = null,
    @get:Schema(description = "Expected response")
    override val response: ResponseDto,
    @get:Schema(description = "Map with property name and expected value")
    val paramMap: Map<String, String>
) : RequestDto()

data class IsOneOfRequestDto(
    @get:Schema(description = "Http method")
    override val method: HttpMethod,
    @get:Schema(description = "Http headers", required = false)
    override val headers: Map<String, String>? = null,
    @get:Schema(description = "Expected response")
    override val response: ResponseDto,
    @get:Schema(description = "Map with property name and list expected value")
    val paramMap: Map<String, List<String>>
) : RequestDto()

data class NotEqualsRequestDto(
    @get:Schema(description = "Http method")
    override val method: HttpMethod,
    @get:Schema(description = "Http headers", required = false)
    override val headers: Map<String, String>? = null,
    @get:Schema(description = "Expected response")
    override val response: ResponseDto,
    @get:Schema(description = "Map with property name and not expected value")
    val paramMap: Map<String, String>,
) : RequestDto()