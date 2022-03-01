package com.github.crazytosser46.chameleon.service

import com.github.crazytosser46.chameleon.entity.MockDocument
import com.github.crazytosser46.chameleon.model.MockModel

interface ApiService {
    suspend fun createMock(mockModel: MockModel): MockDocument

    suspend fun getMockById(id: String): MockModel?

    suspend fun getMockByName(name: String): MockModel?

    suspend fun getMocksByPath(path: String): List<MockModel>

    suspend fun getAllMocks(): List<MockModel>

    suspend fun deleteMockById(id: String)

    suspend fun deleteMockByName(name: String)

    suspend fun deleteMocksByPath(path: String)

    suspend fun updateMock(mockModel: MockModel)
}