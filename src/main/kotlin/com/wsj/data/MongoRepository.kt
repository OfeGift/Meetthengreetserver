package com.wsj.data

import com.wsj.models.Celebrity
import com.wsj.models.Ticket
import com.wsj.models.User

interface MongoRepository {
    suspend fun getUserByUsername(username: String): User?

    suspend fun insertUser(user: User): Boolean

    suspend fun addCelebrity(celebrity: Celebrity): Boolean

    suspend fun updateCelebrity(celebrity: Celebrity): Boolean

    suspend fun addTicket(ticket: Ticket): Boolean

    suspend fun updateTicket(ticket: Ticket): Boolean

    suspend fun deleteSelectedCelebrity(ids: List<String>) : Boolean

    suspend fun deleteSelectedTickets(ids: List<String>) : Boolean

    suspend fun getAllCelebrities(skip: Int): List<Celebrity>

    suspend fun getAllTickets(skip: Int): List<Ticket>

    suspend fun getCelebrityById(id: String) : Celebrity?

    suspend fun getAllTicketsForCelebrity(query: String, skip: Int) : List<Ticket>

    suspend fun getTicketById(id: String): Ticket?

}