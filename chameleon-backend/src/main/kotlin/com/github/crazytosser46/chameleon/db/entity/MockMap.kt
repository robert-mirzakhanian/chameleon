package com.github.crazytosser46.chameleon.db.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object MockMap: LongIdTable("mock_map") {
    val url = reference("url", Url)
    val request = reference("request", Request)
    val response = reference("response", Response)
}

class MockMapDao(id: EntityID<Long>): LongEntity(id) {
    companion object: LongEntityClass<MockMapDao>(MockMap)

    var url by UrlDao referencedOn MockMap.url
    var request by RequestDao referencedOn MockMap.request
    var response by ResponseDao referencedOn MockMap.response
}