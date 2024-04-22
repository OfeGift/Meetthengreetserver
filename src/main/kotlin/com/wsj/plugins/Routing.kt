package com.wsj.plugins

import com.wsj.data.MongoRepository
import com.wsj.routes.*
import com.wsj.security.hashing.HashingServiceRepository
import com.wsj.security.token.TokenConfig
import com.wsj.security.token.TokenServiceRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    mongoRepository: MongoRepository,
    hashingServiceRepository: HashingServiceRepository,
    tokenServiceRepository: TokenServiceRepository,
    tokenConfig: TokenConfig
) {
    routing {
       signIn(mongoRepository, hashingServiceRepository, tokenServiceRepository, tokenConfig)
        signUp(hashingServiceRepository, mongoRepository)
        authenticate()
        getSecretInfo()
        hello()
        addCelebrity(mongoRepository)
        addTicket(mongoRepository)
        getAllCelebrity(mongoRepository)
        getAllTickets(mongoRepository)
        getCelebrityById(mongoRepository)
        getAllCelebrityTicket(mongoRepository)
        deleteSelectedCelebrity(mongoRepository)
        deleteSelectedTickets(mongoRepository)
        updateCelebrity(mongoRepository)
        updateTicket(mongoRepository)
    }
}
