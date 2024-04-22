package com.wsj.di

import com.wsj.Utils.DATABASE_NAME
import com.wsj.data.MongoDB
import com.wsj.data.MongoRepository
import com.wsj.security.hashing.HashingServiceImpl
import com.wsj.security.hashing.HashingServiceRepository
import com.wsj.security.token.TokenConfig
import com.wsj.security.token.TokenServiceImpl
import com.wsj.security.token.TokenServiceRepository
import org.koin.core.scope.get
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase(DATABASE_NAME)
    }

    single<MongoRepository> {
        MongoDB(get())
    }

    single<TokenServiceRepository> {
        TokenServiceImpl()
    }

    single <HashingServiceRepository>{
        HashingServiceImpl()
    }
}