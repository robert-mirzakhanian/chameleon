package com.github.mzr.chameleon.model

import io.swagger.v3.oas.annotations.media.Schema

data class MockDto(
    @Schema(description = "Mock document id", example = "8c5fe5d8-8d15-11ed-a1eb-0242ac120002}")
    val id: String? = null,
    @Schema(description = "Mock name", example = "Mock authentication server")
    val name: String,
    @Schema(description = "Path subbing", example = "/api/authenticate")
    val path: String,
    @Schema(description = "Is active stub", example = "true")
    val active: Boolean,
    @Schema(
        type = "object",
        description = "Expected request for stubbing",
        discriminatorProperty = "operation",
//        oneOf = [AnyRequestDto::class, IsOneOfRequestDto::class, EqualsRequestDto::class, NotEqualsRequestDto::class],
//        subTypes = [AnyRequestDto::class, IsOneOfRequestDto::class, EqualsRequestDto::class, NotEqualsRequestDto::class]
    )
    val requestDtoList: List<RequestDto>
)