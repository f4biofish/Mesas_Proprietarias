package com.librarys.ferreira.core.domain.model.model


sealed class AccountPropFirm{

    abstract val info: AccountInfo

    data class Challenge(
        override val info: AccountInfo,
        val targetProfit: Double
    ) : AccountPropFirm()

    data class Funded(
        override val info: AccountInfo
    ) : AccountPropFirm()

}
