package com.wsj.security.token

interface TokenServiceRepository {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String
}