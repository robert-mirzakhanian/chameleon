package com.github.crazytosser46.chameleon.service

import com.github.crazytosser46.chameleon.entity.*
import com.github.crazytosser46.chameleon.model.*
import com.github.crazytosser46.chameleon.repository.MockRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ApiService {
    suspend fun createMock(mockModel: MockModel)
}


@Service
class ApiServiceImpl : ApiService {

    @Autowired
    private lateinit var mockRepository: MockRepository

    override suspend fun createMock(mockModel: MockModel) {

        val document = mockRepository.findFirstByUri(mockModel.uri)?.let { mockDocument ->
            val newList = mutableListOf<RequestDocument>()
            newList.addAll(mockDocument.requests)
            mockModel.requestModels.forEach { requestModel ->
                newList.add(createRequestDocument(requestModel))
            }
            mockDocument.requests = newList
            mockDocument
        } ?: MockDocument(
            name = mockModel.name,
            url = mockModel.uri,
            isActive = true,
            requests = mockModel.requestModels.map {
                createRequestDocument(it)
            }.toMutableList()
        )

        mockRepository.save(document)
    }

    private fun createRequestDocument(requestModel: RequestModel): RequestDocument {

        val responseDocument = ResponseDocument(
            value = requestModel.response.value ?: "",
            status = requestModel.response.status,
            headers = requestModel.response.header
        )

        return when (requestModel) {
            is EqualsRequestModel ->
                EqualsRequestDocument(
                    isActive = true,
                    method = requestModel.method,
                    responseDocument = responseDocument,
                    headers = requestModel.headers,
                    expectedParams = requestModel.paramMap
                )

            is IsOneOfRequestModel ->
                IsOneOfRequestDocument(
                    isActive = true,
                    method = requestModel.method,
                    responseDocument = responseDocument,
                    headers = requestModel.headers,
                    expectedParams = requestModel.paramMap
                )
            is NotEqualsRequestModel ->
                NotEqualsRequestDocument(
                    isActive = true,
                    method = requestModel.method,
                    responseDocument = responseDocument,
                    headers = requestModel.headers,
                    expectedParams = requestModel.paramMap
                )
            is AnyRequestModel ->
                AnyRequestDocument(
                    isActive = true,
                    method = requestModel.method,
                    responseDocument = responseDocument,
                    headers = requestModel.headers
                )
        }
    }
}