package com.github.crazytosser46.chameleon.model

data class MockModel(
    val id: String? = null,
    val name: String,
    val path: String,
    val active: Boolean,
    val requestModels: List<RequestModel>
)