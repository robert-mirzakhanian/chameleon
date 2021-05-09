package com.github.crazytosser46.chameleon.db.migration

import com.github.crazytosser46.chameleon.db.entity.*
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class V1__Database: BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Request)
            SchemaUtils.create(Response)
            SchemaUtils.create(Header)
            SchemaUtils.create(Url)
            SchemaUtils.create(MockMap)
        }
    }
}