package com.digitalwallet.auth.driven.user.revision.v1

import com.digitalwallet.auth.domain.models.User
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class AggregateV1(val user: User) {

    val id: String = user.id.toString()
    val name: String = "User"
    val snapshot: String = createSnapshot()

    private fun createSnapshot(): String {
        val mapper: ObjectMapper = jacksonObjectMapper()
        return mapper.writeValueAsString(
            mapOf(
                "id" to user.id,
                "email" to user.email,
                "username" to user.username,
                "password" to user.password,
                "createdAt" to user.createdAt.toString()
            )
        )
    }
}
