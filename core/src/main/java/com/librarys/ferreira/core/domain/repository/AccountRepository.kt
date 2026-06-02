package com.librarys.ferreira.core.domain.repository

import com.librarys.ferreira.core.domain.model.model.AccountInfo
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    /**
     * Adiciona os dados de uma nova conta de mesa proprietária no banco de dados
     * @param accountInfo Dados da conta
     * @return true se a conta foi salva com sucesso, false caso contrário
     */
    suspend fun saveAccountInDb(accountInfo: AccountInfo) : Boolean

    /**
     * Retorna o fluxo de todas as contas salvas no banco de dados
     * @return Flow contendo a lista de contas
     */
    fun getAccounts(): Flow<List<AccountInfo>>

    /**
     * Atualiza os dados de uma conta de mesa proprietária no banco de dados
     * @param accountInfo Dados da conta
     * @return true se a conta foi atualizada com sucesso, false caso contrário
     */
    suspend fun updateAccount(accountInfo: AccountInfo): Boolean

}