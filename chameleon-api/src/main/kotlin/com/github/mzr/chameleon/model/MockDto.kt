package com.github.mzr.chameleon.model

data class MockDto(
    val id: String? = null,
    val name: String,
    val path: String,
    val active: Boolean,
    val requestDtos: List<RequestDto>
)