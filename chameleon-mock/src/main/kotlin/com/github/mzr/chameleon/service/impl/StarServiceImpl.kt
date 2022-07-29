package com.github.mzr.chameleon.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.mzr.chameleon.entity.*
import com.github.mzr.chameleon.exception.NotFoundMockException
import com.github.mzr.chameleon.repository.MockRepository
import com.github.mzr.chameleon.service.StarService

import com.nfeld.jsonpathkt.extension.read
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service

@Service
class StarServiceImpl(
    private val mockRepository: MockRepository,
    private val matcherOperationComparator: Comparator<RequestDocument>,
    private val objectMapper: ObjectMapper
) : StarService {

    @Throws(NotFoundMockException::class)
    override suspend fun getMockResponse(serverHttpRequest: ServerHttpRequest): ResponseEntity<Any> {
        val path = serverHttpRequest.path.toString()
        val mockDocument = mockRepository.findFirstByPath(path).awaitFirstOrNull()
            ?: throw NotFoundMockException("Not found mock with $path")
        val request = mockDocument.requests.sortedWith(matcherOperationComparator).filter { it.isActive }.first {
            predicateRequest(it, serverHttpRequest)
        }

        val httpHeaders = HttpHeaders()
        val responseDocument = request.responseDocument
        responseDocument.headers?.map { (key, value) ->
            httpHeaders[key] = value
        }
        return ResponseEntity<Any>(responseDocument.value, httpHeaders, responseDocument.status)
    }

    private suspend fun predicateRequest(
        requestDocument: RequestDocument,
        serverHttpRequest: ServerHttpRequest
    ): Boolean {
        return when (requestDocument) {
            is AnyRequestDocument -> {
                val resultHeader = compareHeader(requestDocument, serverHttpRequest.headers)
                val resultMethod = compareMethod(requestDocument, serverHttpRequest.method)
                resultHeader && resultMethod
            }
            is EqualsRequestDocument -> {
                val resultHeader = compareHeader(requestDocument, serverHttpRequest.headers)
                val resultMethod = compareMethod(requestDocument, serverHttpRequest.method)
                val resultCompare = compareDataByMethod(requestDocument, serverHttpRequest)
                resultHeader && resultMethod && resultCompare
            }
            is IsOneOfRequestDocument -> {
                val resultHeader = compareHeader(requestDocument, serverHttpRequest.headers)
                val resultMethod = compareMethod(requestDocument, serverHttpRequest.method)
                val resultCompare = compareDataByMethod(requestDocument, serverHttpRequest)
                resultHeader && resultMethod && resultCompare
            }
            is NotEqualsRequestDocument -> {
                val resultHeader = compareHeader(requestDocument, serverHttpRequest.headers)
                val resultMethod = compareMethod(requestDocument, serverHttpRequest.method)
                val resultCompare = compareDataByMethod(requestDocument, serverHttpRequest)
                resultHeader && resultMethod && resultCompare
            }
        }
    }

    private fun compareMethod(
        requestDocument: RequestDocument,
        method: HttpMethod?
    ) = requestDocument.method == method

    private fun compareHeader(
        requestDocument: RequestDocument,
        requestHeader: HttpHeaders
    ) = requestDocument.headers?.let { map ->
        map.map { (key, value) ->
            val mutableList = requestHeader.getValuesAsList(key)
            mutableList.contains(value)
        }.fold(initial = true) { acc: Boolean, next: Boolean ->
            acc && next
        }
    } ?: true

    private suspend fun compareDataByMethod(
        equalsRequestDocument: EqualsRequestDocument,
        serverHttpRequest: ServerHttpRequest
    ): Boolean {
        return if (serverHttpRequest.method == HttpMethod.GET) {
            compareQueryParam(equalsRequestDocument, serverHttpRequest.queryParams.toSingleValueMap())
        } else {
            compareParamsFromJson(equalsRequestDocument, getJson(serverHttpRequest))
        }
    }

    private suspend fun compareDataByMethod(
        isOneOfRequestDocument: IsOneOfRequestDocument,
        serverHttpRequest: ServerHttpRequest
    ): Boolean {
        return if (serverHttpRequest.method == HttpMethod.GET) {
            compareQueryParam(isOneOfRequestDocument, serverHttpRequest.queryParams.toSingleValueMap())
        } else {
            compareParamsFromJson(isOneOfRequestDocument, getJson(serverHttpRequest))
        }
    }

    private suspend fun compareDataByMethod(
        notEqualsRequestDocument: NotEqualsRequestDocument,
        serverHttpRequest: ServerHttpRequest
    ): Boolean {
        return if (serverHttpRequest.method == HttpMethod.GET) {
            compareQueryParam(notEqualsRequestDocument, serverHttpRequest.queryParams.toSingleValueMap())
        } else {
            compareParamsFromJson(notEqualsRequestDocument, getJson(serverHttpRequest))
        }
    }

    private fun compareQueryParam(equalsRequestDocument: EqualsRequestDocument, queryParam: Map<String, String>): Boolean {
        return equalsRequestDocument.expectedParams.map { (key, value) ->
            val actualValue = queryParam[key]
            value == actualValue
        }.fold(initial = true) { acc: Boolean, next: Boolean ->
            acc && next
        }
    }

    private fun compareQueryParam(isOneOfRequestDocument: IsOneOfRequestDocument, queryParam: Map<String, String>): Boolean {
        return isOneOfRequestDocument.expectedParams.map { (key, value) ->
            val actualValue = queryParam[key]
            value.contains(actualValue)
        }.fold(initial = true) { acc: Boolean, next: Boolean ->
            acc && next
        }
    }

    private fun compareQueryParam(notEqualsRequestDocument: NotEqualsRequestDocument, queryParam: Map<String, String>): Boolean {
        return notEqualsRequestDocument.expectedParams.map { (key, value) ->
            val actualValue = queryParam[key]
            value != actualValue
        }.fold(initial = true) { acc: Boolean, next: Boolean ->
            acc && next
        }
    }

    private fun compareParamsFromJson(equalsRequestDocument: EqualsRequestDocument, json: String): Boolean {
        val jsonNode = objectMapper.readTree(json)
        return equalsRequestDocument.expectedParams.map { (key, value) ->
            val actualValue = jsonNode.read<String>(key)
            value == actualValue
        }.fold(initial = true) { acc: Boolean, next: Boolean ->
            acc && next
        }
    }

    private fun compareParamsFromJson(isOneOfRequestDocument: IsOneOfRequestDocument, json: String): Boolean {
        val jsonNode = objectMapper.readTree(json)
        return isOneOfRequestDocument.expectedParams.map { (key, value) ->
            val actualValue = jsonNode.read<String>(key)
            value.contains(actualValue)
        }.fold(initial = true) { acc: Boolean, next: Boolean ->
            acc && next
        }
    }

    private fun compareParamsFromJson(notEqualsRequestDocument: NotEqualsRequestDocument, json: String): Boolean {
        val jsonNode = objectMapper.readTree(json)
        return notEqualsRequestDocument.expectedParams.map { (key, value) ->
            val actualValue = jsonNode.read<String>(key)
            value != actualValue
        }.fold(initial = true) { acc: Boolean, next: Boolean ->
            acc && next
        }
    }


    private suspend fun getJson(serverHttpRequest: ServerHttpRequest): String {
        val charset = serverHttpRequest.headers.contentType?.charset
        return String(serverHttpRequest.body.awaitFirst().asByteBuffer().array(), charset ?: Charsets.UTF_8)
    }
}