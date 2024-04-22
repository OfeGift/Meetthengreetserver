package com.wsj

import com.wsj.data.MongoRepository
import com.wsj.di.mainModule
import com.wsj.plugins.*
import com.wsj.security.hashing.HashingServiceRepository
import com.wsj.security.token.TokenConfig
import com.wsj.security.token.TokenServiceRepository
import io.ktor.server.application.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {
    install(Koin) {
        modules(mainModule)
    }

    val tokenServiceRepository: TokenServiceRepository by inject()
    val mongoRepository: MongoRepository by inject()
    val hashingServiceRepository: HashingServiceRepository by inject()

    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L *24L,
        secret = System.getenv("JWT_SECRET")
    )

    configureMonitoring()
    configureSerialization()
    configureSecurity(tokenConfig)
    configureRouting(
        mongoRepository = mongoRepository,
        hashingServiceRepository = hashingServiceRepository,
        tokenServiceRepository = tokenServiceRepository,
        tokenConfig = tokenConfig
    )
}
