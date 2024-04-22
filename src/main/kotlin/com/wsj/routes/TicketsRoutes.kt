package com.wsj.routes

import com.wsj.Utils.ID
import com.wsj.Utils.QUERY_PARAM
import com.wsj.Utils.SKIP_PARAM
import com.wsj.data.MongoRepository
import com.wsj.models.Ticket
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.bson.codecs.ObjectIdGenerator

fun Route.addTicket(
    mongoRepository: MongoRepository,
) {
    post ("/addticket"){
        try {
            val ticket = call.receive<Ticket>()
            val newTicket = ticket.copy(_id = ObjectIdGenerator().generate().toString())
            val inserted = coroutineScope {
                async { mongoRepository.addTicket(newTicket)}
            }.await()
            if (inserted) {
                call.respond(HttpStatusCode.Created, "Ticket added successfully")
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }catch (e: Exception) {
            call.respond(e.message.toString())
        }
    }
}

fun Route.getAllTickets(
    mongoRepository: MongoRepository
) {
    get ("/getalltickets"){
        try {
            val tickets = mongoRepository.getAllTickets(
                skip = SKIP_PARAM.toInt()
            )
            call.respond(HttpStatusCode.OK, tickets)
        } catch (e: Exception) {
            call.respond(e.message.toString())
        }
    }
}

fun Route.getAllCelebrityTicket(
    mongoRepository: MongoRepository
){
    get("/getallcelebrityticket") {
        try {
            val query = call.parameters[QUERY_PARAM] ?: ""
            val skip = call.parameters[SKIP_PARAM]?.toInt() ?: 0
            val tickets = mongoRepository.getAllTicketsForCelebrity(
                query = query,
                skip = skip
            )
            call.respond(HttpStatusCode.OK, tickets)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error occurred while processing request: ${e.message}")
        }
    }
}

fun Route.deleteSelectedTickets(
    mongoRepository: MongoRepository
) {
    delete("/deleteselectedtickets/{id}"){
        try {
            val id = call.parameters[ID] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing Ticket ID")
            val delete = mongoRepository.deleteSelectedTickets(listOf(id))
            if (delete) {
                call.respond(HttpStatusCode.OK, "Tickets with ID: $id Deleted Successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "Tickets with ID: $id not found")
            }
        }catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error occurred while processing request: ${e.message}")
        }
    }
}

fun Route.updateTicket(
    mongoRepository: MongoRepository
) {
    put ("/updateticket"){
        try {
            val ticket = call.receive<Ticket>()
            val update = mongoRepository.updateTicket(ticket)
            if (update) {
                call.respond(HttpStatusCode.OK, "Ticket Updated Successfully.")
            } else {
                call.respond(HttpStatusCode.NotFound, "Ticket with the ID: ${ticket._id} not found.")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error while updating ticket: ${e.message}")
        }
    }
}