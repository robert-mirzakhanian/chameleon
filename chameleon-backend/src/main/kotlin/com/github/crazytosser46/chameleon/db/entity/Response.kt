package com.github.crazytosser46.chameleon.db.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Response: LongIdTable("response") {
    val value = varchar("value", Int.MAX_VALUE)
    val isCompressed = bool("isCompressed")
    val status = integer("status")
    val header = reference("header", Header)
}

class ResponseDao(id: EntityID<Long>): LongEntity(id) {
    companion object: LongEntityClass<ResponseDao>(Response)

    var value by Response.value
    var isCompressed by Response.isCompressed
    var status by Response.status
    var header by HeaderDao referencedOn Response.header
}