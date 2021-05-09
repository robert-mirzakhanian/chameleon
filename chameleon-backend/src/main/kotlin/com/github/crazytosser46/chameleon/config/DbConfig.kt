package com.github.crazytosser46.chameleon.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource
import kotlin.math.max

object DbConfig {

    fun createDataSource(env: ApplicationEnvironment): DataSource {
        val url = env.config.property("application.database.url").getString()
        val driver = env.config.property("application.database.driver").getString()
        val user = env.config.property("application.database.user").getString()
        val pwd = env.config.property("application.database.password").getString()
        val maxPoolSize = env.config.property("application.database.maxPoolSize").getString()

        return HikariDataSource(
            HikariConfig().apply {
                jdbcUrl = url
                driverClassName = driver
                username = user
                password = pwd
                maximumPoolSize = maxPoolSize.toInt()
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                validate()
            }
        )
    }

}