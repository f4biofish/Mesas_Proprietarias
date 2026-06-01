package com.librarys.ferreira.core.data.mapper

import com.google.gson.Gson
import com.librarys.ferreira.core.data.local.entities.AccountEntity
import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.model.rules.AccountRules
import java.util.Date

private val gson = Gson()


fun AccountEntity.toDomain() : AccountInfo {
    return AccountInfo(
        id = this.id,
        numberAccount = this.numberAccount,
        propFirm = PropFirm.valueOf(this.propFirm),
        accountName = this.accountName,
        dayStarting = Date(this.dayStarting),
        dayBroken = dayBroken?.let { Date(it) },
        initialBalance = this.initialBalance,
        currentBalance = this.currentBalance,
        typeDrawdown = DrawnDownTypes.valueOf(this.typeDrawdown),
        maxDrawdownAmmount = this.maxDrawdownAmmount,
        dailyLossLimit = this.dailyLossLimit,
        rulesPropFirm = rulesPropFirm.map { json ->
            gson.fromJson(json, AccountRules::class.java)
        }
    )
}


fun AccountInfo.toEntity() : AccountEntity {
    return AccountEntity(
        id = this.id,
        numberAccount = this.numberAccount,
        propFirm = this.propFirm.name,
        accountName = this.accountName,
        dayStarting = this.dayStarting.time,
        dayBroken = this.dayBroken?.time,
        initialBalance = this.initialBalance,
        currentBalance = this.currentBalance,
        typeDrawdown = this.typeDrawdown.name,
        maxDrawdownAmmount = this.maxDrawdownAmmount,
        dailyLossLimit = this.dailyLossLimit,
        rulesPropFirm = this.rulesPropFirm.map { rule ->
            gson.toJson(rule)
        }
    )
}