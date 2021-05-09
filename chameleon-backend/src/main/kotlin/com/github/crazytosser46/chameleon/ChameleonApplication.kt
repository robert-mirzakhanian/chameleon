package com.github.crazytosser46.chameleon

import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.crazytosser46.chameleon.config.DbConfig
import com.github.crazytosser46.chameleon.db.migration.V1__Database
import com.github.crazytosser46.chameleon.route.apiRoute
import com.github.crazytosser46.chameleon.service.ApiService
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import java.text.DateFormat

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Compression)
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            dateFormat = DateFormat.getDateInstance()
        }
    }

    val dataSource = DbConfig.createDataSource(environment)
    Database.connect(dataSource)

    Flyway.configure()
        .dataSource(dataSource)
        .schemas("PUBLIC")
        .javaMigrations(V1__Database())
        .load()
        .migrate()

    val apiService = ApiService()

    routing {
        apiRoute(apiService)
    }
}