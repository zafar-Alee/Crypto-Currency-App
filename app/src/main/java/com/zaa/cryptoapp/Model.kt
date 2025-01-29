package com.zaa.cryptoapp
data class Model(
    val name: String,
    val symbol: String,
    val price: String,
    val marketCap: Long?,
    val marketCapRank: Int?,
    val high24h: Double?,
    val low24h: Double?,
    val circulatingSupply: Double?,
    val totalSupply: Double?,
    val maxSupply: Double?,
    val ath: Double?,
    val atl: Double?
)
