package com.wsj.models

import kotlinx.serialization.Serializable

@Serializable
data class Celebrity(
    val _id: String = "",
    val name: String,
    val celebrityId: String,
    val description: String,
    val imageUrl: String?
)
