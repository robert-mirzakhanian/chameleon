package com.github.crazytosser46.chameleon.db.entity

import com.github.crazytosser46.chameleon.model.MatcherOperation
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Request: LongIdTable("request") {
    val paramPath = varchar("paramPath", 255)
    val matcherOperation = enumerationByName("matcherOperation", 50, MatcherOperation::class)
    val paramValue = varchar("paramValue", 255)
    val paramIsCompressed = bool("paramIsCompressed")
    val header = reference("header", Header)
}

class RequestDao(id: EntityID<Long>): LongEntity(id) {
    companion object: LongEntityClass<RequestDao>(Request)

    var paramPath by Request.paramPath
    var matcherOperation by Request.matcherOperation
    var paramValue by Request.paramValue
    var paramIsCompressed by Request.paramIsCompressed
    var header by HeaderDao referencedOn Request.header
}