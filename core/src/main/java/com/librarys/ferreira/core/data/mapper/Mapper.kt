package com.librarys.ferreira.core.data.mapper

import com.google.gson.Gson
import com.librarys.ferreira.core.data.local.entities.AccountEntity
import com.librarys.ferreira.core.data.local.entities.TradesEntity
import com.librarys.ferreira.core.domain.model.enums.AccountStage
import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.model.model.SymbolAtivo
import com.librarys.ferreira.core.domain.model.model.Trades
import com.librarys.ferreira.core.domain.model.rules.AccountRules
import timber.log.Timber
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
        accountStage = AccountStage.valueOf(this.accountStage),
        rulesPropFirm = rulesPropFirm.mapNotNull { json ->
            try {
                when {
                    json.contains("maxPercentage") ->
                        gson.fromJson(json, AccountRules.ConsistencyRule::class.java)
                    json.contains("minutesBefore") ->
                        gson.fromJson(json, AccountRules.NewsRestrictionRule::class.java)
                    json.contains("daysTrading") ->
                        gson.fromJson(json, AccountRules.MinimumTradingDaysRule::class.java)
                    else -> null
                }
            } catch (e: Exception) {
                Timber.e("Erro na conversão de Account Rules: ${e.message}")
                null
            }
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
        accountStage = this.accountStage.name,
        rulesPropFirm = this.rulesPropFirm.map { rule ->
            gson.toJson(rule)
        }
    )
}

fun TradesEntity.toDomain() : Trades {
    return Trades(
        id = this.id,
        date = Date(this.date),
        accountInfoId = this.accountInfoId,
        accountNumber = this.accountNumber,
        symbolAtivo = SymbolAtivo.valueOf(this.symbolAtivo),
        contratos = this.contratos,
        profit = this.profit
    )
}


fun Trades.toEntity() : TradesEntity {
    return TradesEntity(
        id = this.id,
        date = this.date.time,
        accountInfoId = this.accountInfoId,
        accountNumber = this.accountNumber,
        symbolAtivo = this.symbolAtivo.name,
        contratos = this.contratos,
        profit = this.profit
    )
}