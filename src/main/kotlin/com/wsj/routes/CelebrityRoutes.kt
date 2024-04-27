package com.wsj.routes

import com.wsj.Utils.ID
import com.wsj.Utils.SKIP_PARAM
import com.wsj.data.MongoRepository
import com.wsj.models.Celebrity
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.bson.codecs.ObjectIdGenerator

fun Route.addCelebrity(
    mongoRepository: MongoRepository,
) {
    post ("/addcelebrity"){
        try {
            val celebrity = call.receive<Celebrity>()
            val newCelebrity = celebrity.copy(_id = ObjectIdGenerator().generate().toString())
            val inserted = coroutineScope {
                async { mongoRepository.addCelebrity(newCelebrity) }
            }.await()
            if (inserted) {
                call.respond(HttpStatusCode.Created, "Celebrity added successfully")
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }catch (e: Exception) {
            call.respond(e.message.toString())
        }
    }
}

fun Route.getAllCelebrity(
    mongoRepository: MongoRepository
) {
    get ("/getcelebrities"){
        try {
            val skip = call.parameters[SKIP_PARAM]?.toInt() ?: 0
            val celebrities = mongoRepository.getAllCelebrities(
                skip = skip
            )
            call.respond(HttpStatusCode.OK, celebrities)
        }catch (e: Exception) {
            call.respond(e.message.toString())
        }
    }
}

fun Route.getCelebrityById(
    mongoRepository: MongoRepository
) {
    get ("/getcelebritybyid/{id}"){
        val id = call.parameters[ID] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing ID")
        val celebrity = mongoRepository.getCelebrityById(id)
        try {
            if (celebrity != null) {
                call.respond(HttpStatusCode.OK, celebrity)
            } else {
                call.respond(HttpStatusCode.NotFound, "Celebrity with ID $id not found")
            }
        }catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error occurred while processing request: ${e.message}")
        }
    }
}

fun Route.deleteSelectedCelebrity(
    mongoRepository: MongoRepository
) {
    delete("/deleteselectedcelebrity/{id}") {
        val id = call.parameters[ID] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing ID")
        try {
            val delete = mongoRepository.deleteSelectedCelebrity(listOf(id))
            if (delete) {
                call.respond(HttpStatusCode.OK, "Celebrity with ID: $id Deleted Successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "Celebrity with ID $id not found")
            }
        }catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error occurred while processing request:  ${e.message}")
        }
    }
}

fun Route.updateCelebrity(
    mongoRepository: MongoRepository
) {
    put ("/updatecelebrity"){
        try {
            val celebrity = call.receive<Celebrity>()
            val updated = mongoRepository.updateCelebrity(celebrity)
            if (updated) {
                call.respond(HttpStatusCode.OK, "Celebrity Updated Successfully.")
            } else {
                call.respond(HttpStatusCode.NotFound, "Celebrity with the ID: ${celebrity._id} not found")
            }
        }catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError,  "Error while updating celebrity: ${e.message}")
        }
    }
}