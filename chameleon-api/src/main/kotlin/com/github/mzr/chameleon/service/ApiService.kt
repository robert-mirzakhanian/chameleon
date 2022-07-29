package com.github.mzr.chameleon.service

import com.github.mzr.chameleon.entity.MockDocument
import com.github.mzr.chameleon.model.MockDto

interface ApiService {
    suspend fun createMock(mockDto: MockDto): MockDocument

    suspend fun getMockById(id: String): MockDto?

    suspend fun getMockByName(name: String): MockDto?

    suspend fun getMocksByPath(path: String): List<MockDto>

    suspend fun getAllMocks(): List<MockDto>

    suspend fun deleteMockById(id: String)

    suspend fun deleteMockByName(name: String)

    suspend fun deleteMocksByPath(path: String)

    suspend fun updateMock(mockDto: MockDto)
}