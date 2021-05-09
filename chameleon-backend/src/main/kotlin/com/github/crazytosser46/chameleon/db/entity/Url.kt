package com.github.crazytosser46.chameleon.db.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Url: LongIdTable("url") {
    val value = varchar("value", 255).index()
}

class UrlDao(id: EntityID<Long>): LongEntity(id) {
    companion object: LongEntityClass<UrlDao>(Url)
    var value by Url.value
}