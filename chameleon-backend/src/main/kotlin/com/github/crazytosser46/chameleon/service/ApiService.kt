package com.github.crazytosser46.chameleon.service

import com.github.crazytosser46.chameleon.model.EqualsRequestModel
import com.github.crazytosser46.chameleon.model.RequestModel
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class ApiService {

    private val log = LoggerFactory.getLogger(ApiService::class.java)

    fun addRequest(requestModel: RequestModel): Boolean {
        log.info("Got request for add new mock request")
        return try {

            true
        } catch (ex:Exception) {
            log.error("During work got unexpected exception!", ex)
            false
        }
    }

    private fun createEntityForEqualsRequest(equalsRequestModel: EqualsRequestModel) {
        equalsRequestModel

        transaction {

        }
    }
}