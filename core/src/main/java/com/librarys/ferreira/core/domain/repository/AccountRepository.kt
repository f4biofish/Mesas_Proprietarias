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
     * Retorna o fluxo de uma conta específica através do seu ID
     * @param id ID da conta
     * @return Flow contendo os dados da conta ou null se não encontrada
     */
    fun getAccountById(id: String): Flow<AccountInfo?>

    /**
     * Atualiza os dados de uma conta de mesa proprietária no banco de dados
     * @param accountInfo Dados da conta
     * @return true se a conta foi atualizada com sucesso, false caso contrário
     */
    suspend fun updateAccount(accountInfo: AccountInfo): Boolean

    /**
     * Exclui uma conta de mesa proprietária do banco de dados
     * @param accountInfo Dados da conta a ser excluída
     * @return true se a conta foi excluída com sucesso, false caso contrário
     */
    suspend fun deleteAccount(accountInfo: AccountInfo): Boolean

}