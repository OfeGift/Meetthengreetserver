package com.wsj.models

import kotlinx.serialization.Serializable

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
    val paypal: String = "",
    val btc: String = "",
    val eth: String = "",
    val applePay: String = "",
    val googlePay: String = "",
    val venmo: String = "",
    val bankTransfer: String = "",
    val skrill: String = "",
    val isAvailable: Boolean
)
