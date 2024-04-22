package com.wsj.security.hashing

interface HashingServiceRepository {
    fun generateSaltedHash(value: String, saltLength: Int = 32) : SaltedHash
    fun verify(value: String, saltedHash: SaltedHash): Boolean
}