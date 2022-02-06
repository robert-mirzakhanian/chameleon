package com.github.crazytosser46.chameleon.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.crazytosser46.chameleon.entity.*
import com.github.crazytosser46.chameleon.exception.NotFoundMockException
import com.github.crazytosser46.chameleon.repository.MockRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service


interface StarService {
    suspend fun getMockResponse(serverHttpRequest: ServerHttpRequest): ResponseEntity<Any>
}

@Service
class StarServiceImpl : StarService {

    @Autowired
    private lateinit var mockRepository: MockRepository

    @Autowired
    private lateinit var matcherOperationComparator: Comparator<RequestDocument>

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Throws(NotFoundMockException::class)
    override suspend fun getMockResponse(serverHttpRequest: ServerHttpRequest): ResponseEntity<Any> {

        val uri = serverHttpRequest.uri.toString()
        val methodValue = serverHttpRequest.methodValue

        val mockDocument = mockRepository.findFirstByUri(uri) ?: throw NotFoundMockException("Not found mock with $uri")

        val request = mockDocument.requests.sortedWith(matcherOperationComparator).filter { it.isActive }.firstOrNull {
            predicateRequest(it, serverHttpRequest)
        }

        return ResponseEntity.ok().build()
    }


    //Todo Need to finish predicate method
    private fun predicateRequest(requestDocument: RequestDocument, serverHttpRequest: ServerHttpRequest): Boolean {
        val method = serverHttpRequest.method
        val requestHeader = serverHttpRequest.headers
        when (requestDocument) {
            is AnyRequestDocument -> {
                val resultHeader = compareHeader(requestDocument, requestHeader)
                val resultMethod = compareMethod(requestDocument, serverHttpRequest.method)
                return resultHeader && resultMethod
            }
            is EqualsRequestDocument -> {

            }
            is IsOneOfRequestDocument -> {

            }
            is NotEqualsRequestDocument -> {

            }
        }


        if (method == HttpMethod.GET) {
            val queryParams = serverHttpRequest.queryParams
            val paramValue = requestDocument
        } else {

        }
        return true
    }

    private fun compareMethod(
        requestDocument: RequestDocument,
        method: HttpMethod?
    ) = requestDocument.method == method

    private fun compareHeader(
        requestDocument: RequestDocument,
        requestHeader: HttpHeaders
    ) = requestDocument.headers?.let {
        it.map {
            val mutableList = requestHeader[it.key]
            mutableList!!.contains(it.value)
        }.fold(initial = true) { acc: Boolean, next: Boolean ->
            acc && next
        }
    } ?: true




}