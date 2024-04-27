package com.wsj.routes

import com.wsj.data.MongoRepository
import com.wsj.data.requests.AuthRequest
import com.wsj.data.responses.AuthResponse
import com.wsj.models.User
import com.wsj.security.hashing.HashingServiceRepository
import com.wsj.security.hashing.SaltedHash
import com.wsj.security.token.TokenClaim
import com.wsj.security.token.TokenConfig
import com.wsj.security.token.TokenServiceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.codec.digest.DigestUtils

fun Route.signUp(
    hashingServiceRepository: HashingServiceRepository,
    userDataSource: MongoRepository
) {
    post("/signup") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, "Bad request")
            return@post
        }

        val inputFieldsBlank = request.username.isBlank() || request.password.isBlank()
        val weakPassword = request.password.length < 8
        if (inputFieldsBlank) {
            call.respond(HttpStatusCode.Conflict, "Input fields empty")
            return@post
        }

        if (weakPassword) {
            call.respond(HttpStatusCode.Conflict, "Password is too weak")
            return@post
        }

        // Check if username exists
        val existingUser = userDataSource.getUserByUsername(request.username)
        if (existingUser != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists. Please try signing in.")
            return@post
        }

        val saltedHash = hashingServiceRepository.generateSaltedHash(request.password)
        val user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

        val wasAcknowledged = userDataSource.insertUser(user)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK, "User sign Up successful.")
    }
}

fun Route.signIn(
    userDataSource: MongoRepository,
    hashingServiceRepository: HashingServiceRepository,
    tokenServiceRepository: TokenServiceRepository,
    tokenConfig: TokenConfig
) {
    post("/signin") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userDataSource.getUserByUsername(request.username)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "User does not exist")
            return@post
        }

        val validPassword = hashingServiceRepository.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (!validPassword) {
            println("Entered hash: ${DigestUtils.sha256Hex("${user.salt}${request.password}")}, Hashed PW: ${user.password}")
            call.respond(HttpStatusCode.Conflict, "Incorrect Password.")
            return@post
        }

        val token = tokenServiceRepository.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            )
        )
    }
}

fun Route.authenticate() {
    authenticate {
        get("/authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate{
        get("/secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your UserId is $userId")
        }
    }
}

fun Route.hello(){
    get("/") {
        call.respondText("Hello!..Welcome to Meetthengreet-server.")
    }
}