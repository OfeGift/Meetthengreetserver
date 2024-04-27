package com.wsj.data

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.wsj.Utils.TICKET_LIMIT
import com.wsj.models.Celebrity
import com.wsj.models.Ticket
import com.wsj.models.User
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class MongoDB(
    db: MongoDatabase
): MongoRepository {

    private val users = db.getCollection<User>("user")
    private val celebrities = db.getCollection<Celebrity>("celebrity")
    private val tickets = db.getCollection<Ticket>("ticket")

    override suspend fun getUserByUsername(username: String): User? {
        return users.find(Filters.eq(User::username.name, username)).firstOrNull()
    }

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(
            user
        ).wasAcknowledged()
    }

    override suspend fun addCelebrity(celebrity: Celebrity): Boolean {
        return celebrities.insertOne(celebrity).wasAcknowledged()
    }

    override suspend fun updateCelebrity(celebrity: Celebrity): Boolean {
        val filter = Filters.eq(Celebrity::_id.name, celebrity._id)
        val update = Updates.combine(
            mutableListOf(
                Updates.set(Celebrity::name.name, celebrity.name),
                Updates.set(Celebrity::celebrityId.name, celebrity.celebrityId),
                Updates.set(Celebrity::description.name, celebrity.description),
                Updates.set(Celebrity::imageUrl.name, celebrity.imageUrl)
            )
        )
        val updateResult = celebrities.withDocumentClass<Celebrity>().updateOne(filter, update)
        return updateResult.wasAcknowledged()
    }

    override suspend fun addTicket(ticket: Ticket): Boolean {
        return tickets.insertOne(ticket).wasAcknowledged()
    }

    override suspend fun updateTicket(ticket: Ticket): Boolean {
        val filter = Filters.eq(Ticket::_id.name, ticket._id)
        val update = Updates.combine(
            mutableListOf(
                Updates.set(Ticket::celebrityId.name, ticket.celebrityId),
                Updates.set(Ticket::section.name, ticket.section),
                Updates.set(Ticket::seatNumber.name, ticket.seatNumber),
                Updates.set(Ticket::price.name, ticket.price),
                Updates.set(Ticket::date.name, ticket.date),
                Updates.set(Ticket::startTime.name, ticket.startTime),
                Updates.set(Ticket::location.name, ticket.location),
                Updates.set(Ticket::description.name, ticket.description),
                Updates.set(Ticket::paypal.name, ticket.paypal),
                Updates.set(Ticket::btc.name, ticket.btc),
                Updates.set(Ticket::eth.name, ticket.eth),
                Updates.set(Ticket::applePay.name, ticket.applePay),
                Updates.set(Ticket::googlePay.name, ticket.googlePay),
                Updates.set(Ticket::venmo.name, ticket.venmo),
                Updates.set(Ticket::bankTransfer.name, ticket.bankTransfer),
                Updates.set(Ticket::skrill.name, ticket.skrill),
                Updates.set(Ticket::isAvailable.name, ticket.isAvailable)
            )
        )
        val updateResult = tickets.withDocumentClass<Ticket>().updateOne(filter, update)
        return updateResult.wasAcknowledged()
    }

    override suspend fun deleteSelectedCelebrity(ids: List<String>): Boolean {
        return celebrities
            .withDocumentClass<Celebrity>()
            .deleteMany(Filters.`in`(Celebrity::_id.name, ids))
            .wasAcknowledged()
    }

    override suspend fun deleteSelectedTickets(ids: List<String>): Boolean {
        return tickets
            .withDocumentClass<Ticket>()
            .deleteMany(Filters.`in`(Ticket::_id.name, ids))
            .wasAcknowledged()
    }

    override suspend fun getAllCelebrities(skip: Int): List<Celebrity> {
        return celebrities
            .withDocumentClass<Celebrity>()
            .find()
            .skip(skip)
            .limit(TICKET_LIMIT)
            .toList()
    }

    override suspend fun getAllTickets(skip: Int): List<Ticket> {
        return tickets
            .withDocumentClass<Ticket>()
            .find()
            .skip(skip)
            .limit(TICKET_LIMIT)
            .toList()
    }

    override suspend fun getCelebrityById(id: String): Celebrity? {
        return celebrities
            .withDocumentClass<Celebrity>()
            .find(
                Filters.eq(
                    Celebrity::_id.name, id
                )
            )
            .toList()
            .firstOrNull()
    }

    override suspend fun getAllTicketsForCelebrity(query: String, skip: Int): List<Ticket> {
//        val regexQuery = query.toRegex(RegexOption.IGNORE_CASE)
        return tickets
            .withDocumentClass<Ticket>()
            .find(
                Filters.eq(
                    Ticket::celebrityId.name, query
                )
            )
            .sort(ascending(Ticket::date.name))
            .skip(skip)
            .limit(TICKET_LIMIT)
            .toList()
    }

    override suspend fun getTicketById(id: String): Ticket {
        return tickets
            .find(
                Filters.eq(
                    Ticket::_id.name, id
                )
            )
            .toList()
            .first()
    }
}