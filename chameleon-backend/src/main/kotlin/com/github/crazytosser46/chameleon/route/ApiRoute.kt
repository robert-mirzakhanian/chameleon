package com.github.crazytosser46.chameleon.route

import com.github.crazytosser46.chameleon.db.entity.HeaderDao
import com.github.crazytosser46.chameleon.model.RequestModel
import com.github.crazytosser46.chameleon.service.ApiService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.apiRoute(apiService: ApiService) {
    routing {
        route("/api") {
            route("/request") {
                post("/add") {
                    val receive = call.receive<RequestModel>()
                    if (apiService.addRequest(receive)) {
                        call.response.status(HttpStatusCode.Created)
                    } else {
                        call.response.status(HttpStatusCode.InternalServerError)
                        call.respond("Unexpected exception!")
                    }
                }
            }
        }
    }
}