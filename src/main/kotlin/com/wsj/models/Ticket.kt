package com.wsj.models

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Ticket(
    val _id: String = "",
    val celebrityId: String,
    val section: String,
    val seatNumber: Int,
    val price: Double,
    val date: String,
    val startTime: String,
    val location : String,
    val description: String,
    val isAvailable: Boolean
)
