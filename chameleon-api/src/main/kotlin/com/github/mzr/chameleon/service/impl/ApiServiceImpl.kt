package com.github.mzr.chameleon.service.impl

import com.github.mzr.chameleon.entity.*
import com.github.mzr.chameleon.model.*
import com.github.mzr.chameleon.repository.MockRepository
import com.github.mzr.chameleon.service.ApiService
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class ApiServiceImpl : ApiService {

    @Autowired
    private lateinit var mockRepository: MockRepository

    override suspend fun createMock(mockDto: MockDto): MockDocument {
        val document = MockDocument(
            name = mockDto.name,
            path = mockDto.path,
            active = mockDto.active,
            requests = mockDto.requestDtoList.map {
                createRequestDocument(it)
            }.toMutableList()
        )
        return mockRepository.save(document).awaitFirst()
    }

    override suspend fun getMockById(id: String): MockDto {
        val result = runCatching { mockRepository.findFirstById(id).awaitSingle() }
        if (result.isFailure) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, result.exceptionOrNull()!!.message)
        }
        return mapMockDocumentToMockModel(result.getOrNull())!!
    }

    override suspend fun getMockByName(name: String): MockDto {
        val result = kotlin.runCatching { mockRepository.findFirstByName(name).awaitSingle() }
        if (result.isFailure) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, result.exceptionOrNull()!!.message)
        }
        return mapMockDocumentToMockModel(result.getOrNull())!!
    }

    override suspend fun getMocksByPath(path: String): List<MockDto> {
        return mockRepository.findAllByPath(path).collectList().awaitFirst()
            .mapNotNull { mapMockDocumentToMockModel(it) }
    }

    override suspend fun getAllMocks(pageable: Pageable): List<MockDto> {
        return mockRepository.findAllBy(pageable).collectList().awaitFirst()
            .mapNotNull { mapMockDocumentToMockModel(it) }
    }

    override suspend fun deleteMockById(id: String) {
        mockRepository.deleteAllById(mutableListOf(id)).awaitFirst()
    }

    override suspend fun deleteMockByName(name: String) {
        mockRepository.deleteByName(name).awaitFirst()
    }

    override suspend fun deleteMocksByPath(path: String) {
        val listIds = mockRepository.findAllByPath(path)
            .buffer()
            .flatMapIterable { it }
            .collectList()
            .awaitFirst()
            .map { it.id }
        mockRepository.deleteAllById(listIds).awaitFirst()
    }

    override suspend fun updateMock(mockDto: MockDto) {
        val id = mockDto.id!!
        val document = mockRepository.findFirstById(id).awaitFirst()!!
        document.path = mockDto.path
        document.name = mockDto.name
        document.active = mockDto.active
        document.requests = mockDto.requestDtoList.map {
            createRequestDocument(it)
        }
        mockRepository.save(document).awaitFirst()
    }

    private fun <T> checkIsNotNull(expected: T?): T {
        return expected ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }


    private fun createRequestDocument(requestDto: RequestDto): RequestDocument {
        val responseDocument = ResponseDocument(
            value = requestDto.response.value ?: "",
            status = requestDto.response.status,
            headers = requestDto.response.headers
        )

        return when (requestDto) {
            is EqualsRequestDto -> EqualsRequestDocument(
                isActive = true,
                method = requestDto.method,
                responseDocument = responseDocument,
                headers = requestDto.headers,
                expectedParams = requestDto.paramMap
            )

            is IsOneOfRequestDto -> IsOneOfRequestDocument(
                isActive = true,
                method = requestDto.method,
                responseDocument = responseDocument,
                headers = requestDto.headers,
                expectedParams = requestDto.paramMap
            )
            is NotEqualsRequestDto -> NotEqualsRequestDocument(
                isActive = true,
                method = requestDto.method,
                responseDocument = responseDocument,
                headers = requestDto.headers,
                expectedParams = requestDto.paramMap
            )
            is AnyRequestDto -> AnyRequestDocument(
                isActive = true,
                method = requestDto.method,
                responseDocument = responseDocument,
                headers = requestDto.headers
            )
            else -> {
                AnyRequestDocument(
                    isActive = true,
                    method = requestDto.method,
                    responseDocument = responseDocument,
                    headers = requestDto.headers
                )
            }
        }
    }

    private fun createRequestModel(requestDocument: RequestDocument): RequestDto {
        val responseDto = ResponseDto(
            value = requestDocument.responseDocument.value,
            status = requestDocument.responseDocument.status,
            headers = requestDocument.responseDocument.headers ?: emptyMap()
        )

        return when (requestDocument) {
            is EqualsRequestDocument -> EqualsRequestDto(
                method = requestDocument.method,
                response = responseDto,
                headers = requestDocument.headers,
                paramMap = requestDocument.expectedParams
            )

            is IsOneOfRequestDocument -> IsOneOfRequestDto(
                method = requestDocument.method,
                response = responseDto,
                headers = requestDocument.headers,
                paramMap = requestDocument.expectedParams
            )
            is NotEqualsRequestDocument -> NotEqualsRequestDto(
                method = requestDocument.method,
                response = responseDto,
                headers = requestDocument.headers,
                paramMap = requestDocument.expectedParams
            )
            is AnyRequestDocument -> AnyRequestDto(
                method = requestDocument.method, response = responseDto, headers = requestDocument.headers
            )
        }
    }

    private fun mapMockDocumentToMockModel(mockDocument: MockDocument?): MockDto? {
        if (mockDocument == null) return null
        return MockDto(mockDocument.id,
            mockDocument.name,
            mockDocument.path,
            mockDocument.active,
            requestDtoList = mockDocument.requests.map {
                createRequestModel(it)
            })
    }
}