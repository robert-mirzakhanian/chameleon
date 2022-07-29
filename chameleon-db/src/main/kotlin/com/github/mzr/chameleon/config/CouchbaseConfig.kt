package com.github.mzr.chameleon.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration

@Configuration

class CouchbaseConfig(private val couchbaseProperties: CouchbaseProperties) : AbstractCouchbaseConfiguration() {

    override fun getConnectionString(): String? {
        return couchbaseProperties.url
    }

    override fun getUserName(): String? {
        return couchbaseProperties.username
    }

    override fun getPassword(): String? {
        return couchbaseProperties.password
    }

    override fun getBucketName(): String? {
        return couchbaseProperties.bucket
    }
}

@Configuration
@ConfigurationProperties("app.couchbase")
class CouchbaseProperties {
    lateinit var url: String
    lateinit var username: String
    lateinit var password: String
    lateinit var bucket: String
}