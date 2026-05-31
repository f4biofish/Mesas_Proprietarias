package com.librarys.ferreira.mesas_proprietarias.domain.model

data class AssetFee(
    val nome: String,
    val symbol: Ativos, //Ex: "NQ", "MNQ", "ES",
    val feePerContract: Double // Valor total (entrada e saída da operação)
)
