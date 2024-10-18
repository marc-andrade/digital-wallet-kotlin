package com.digitalwallet.driver.transaction

import com.digitalwallet.application.commands.CreateTransactionCommand
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response

@Path("/transactions")
class Endpoint {
    @POST
    fun createTransaction(command: CreateTransactionCommand): Response {

        return Response.ok().build()
    }
}