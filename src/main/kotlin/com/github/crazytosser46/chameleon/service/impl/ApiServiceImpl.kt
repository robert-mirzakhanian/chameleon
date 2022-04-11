package com.github.crazytosser46.chameleon.service.impl

import com.github.crazytosser46.chameleon.entity.*
import com.github.crazytosser46.chameleon.model.*
import com.github.crazytosser46.chameleon.repository.MockRepository
import com.github.crazytosser46.chameleon.service.ApiService
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.util.stream.Stream


@Service
class ApiServiceImpl : ApiService {

    @Autowired
    private lateinit var mockRepository: MockRepository

    override suspend fun createMock(mockModel: MockModel): MockDocument {
        val document = MockDocument(
            name = mockModel.name,
            path = mockModel.path,
            active = mockModel.active,
            requests = mockModel.requestModels.map {
                createRequestDocument(it)
            }.toMutableList()
        )
        return mockRepository.insert(document).awaitFirst()
    }

    override suspend fun getMockById(id: String): MockModel? {
        return mapMockDocumentToMockModel(mockRepository.findFirstById(id).awaitFirst())
    }

    override suspend fun getMockByName(name: String): MockModel? {
        return mapMockDocumentToMockModel(mockRepository.findFirstByName(name).awaitFirst())
    }

    override suspend fun getMocksByPath(path: String): List<MockModel> {
        return mockRepository.findAllByPath(path).collectList().awaitFirst().mapNotNull { mapMockDocumentToMockModel(it) }
    }

    override suspend fun getAllMocks(): List<MockModel> {
        return mockRepository.findAll().collectList().awaitFirst().mapNotNull { mapMockDocumentToMockModel(it) }
    }

    override suspend fun deleteMockById(id: String) {
        mockRepository.deleteAllById(mutableListOf(id)).awaitFirst()
    }

    override suspend fun deleteMockByName(name: String) {
        mockRepository.deleteByName(name).awaitFirst()
    }

    override suspend fun deleteMocksByPath(path: String) {
        val listIds = mockRepository.findAllByPath(path).collectList().awaitFirst().map { it.id }
        mockRepository.deleteAllById(listIds).awaitFirst()
    }

    override suspend fun updateMock(mockModel: MockModel) {
        val id = mockModel.id!!
        val document = mockRepository.findFirstById(id).awaitFirst()!!
        document.path = mockModel.path
        document.name = mockModel.name
        document.active = mockModel.active
        document.requests = mockModel.requestModels.map {
            createRequestDocument(it)
        }
        mockRepository.save(document).awaitFirst()
    }


    private fun createRequestDocument(requestModel: RequestModel): RequestDocument {
        val responseDocument = ResponseDocument(
            value = requestModel.response.value ?: "",
            status = requestModel.response.status,
            headers = requestModel.response.headers
        )

        return when (requestModel) {
            is EqualsRequestModel -> EqualsRequestDocument(
                isActive = true,
                method = requestModel.method,
                responseDocument = responseDocument,
                headers = requestModel.headers,
                expectedParams = requestModel.paramMap
            )

            is IsOneOfRequestModel -> IsOneOfRequestDocument(
                isActive = true,
                method = requestModel.method,
                responseDocument = responseDocument,
                headers = requestModel.headers,
                expectedParams = requestModel.paramMap
            )
            is NotEqualsRequestModel -> NotEqualsRequestDocument(
                isActive = true,
                method = requestModel.method,
                responseDocument = responseDocument,
                headers = requestModel.headers,
                expectedParams = requestModel.paramMap
            )
            is AnyRequestModel -> AnyRequestDocument(
                isActive = true,
                method = requestModel.method,
                responseDocument = responseDocument,
                headers = requestModel.headers
            )
        }
    }

    private fun createRequestModel(requestDocument: RequestDocument): RequestModel {
        val responseModel = ResponseModel(
            value = requestDocument.responseDocument.value,
            status = requestDocument.responseDocument.status,
            headers = requestDocument.responseDocument.headers ?: emptyMap()
        )

        return when (requestDocument) {
            is EqualsRequestDocument -> EqualsRequestModel(
                method = requestDocument.method,
                response = responseModel,
                headers = requestDocument.headers,
                paramMap = requestDocument.expectedParams
            )

            is IsOneOfRequestDocument -> IsOneOfRequestModel(
                method = requestDocument.method,
                response = responseModel,
                headers = requestDocument.headers,
                paramMap = requestDocument.expectedParams
            )
            is NotEqualsRequestDocument -> NotEqualsRequestModel(
                method = requestDocument.method,
                response = responseModel,
                headers = requestDocument.headers,
                paramMap = requestDocument.expectedParams
            )
            is AnyRequestDocument -> AnyRequestModel(
                method = requestDocument.method, response = responseModel, headers = requestDocument.headers
            )
        }
    }

    private fun mapMockDocumentToMockModel(mockDocument: MockDocument?): MockModel? {
        if (mockDocument == null) return null
        return MockModel(mockDocument.id,
            mockDocument.name,
            mockDocument.path,
            mockDocument.active,
            requestModels = mockDocument.requests.map {
                createRequestModel(it)
            })
    }
}