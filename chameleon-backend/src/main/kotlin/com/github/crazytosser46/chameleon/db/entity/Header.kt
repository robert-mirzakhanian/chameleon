package com.github.crazytosser46.chameleon.db.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Header: LongIdTable("header") {
    val key = varchar("key", 255)
    val value = varchar("value", 255)
}

class HeaderDao(id: EntityID<Long>): LongEntity(id) {
    companion object : LongEntityClass<HeaderDao>(Header)

    var key by Header.key
    var value by Header.value
}