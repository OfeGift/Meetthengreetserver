package com.wsj.di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.wsj.Utils.DATABASE_NAME
import com.wsj.data.MongoDB
import com.wsj.data.MongoRepository
import com.wsj.security.hashing.HashingServiceImpl
import com.wsj.security.hashing.HashingServiceRepository
import com.wsj.security.token.TokenServiceImpl
import com.wsj.security.token.TokenServiceRepository
import org.koin.dsl.module

val mainModule = module {
//    val clientConnection = "mongodb://mongodb:27017" //Default without docker is -> "mongodb://localhost"
    val mongoPw = System.getenv("MONGO_PW")
    val clientConnection = "mongodb+srv://ofegift22:$mongoPw@meetthengreet-cluster.rbrnjf6.mongodb.net/?retryWrites=true&w=majority&appName=Meetthengreet-cluster"
    single {
        MongoClient.create(clientConnection)
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