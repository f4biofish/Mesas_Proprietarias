package com.librarys.ferreira.core.domain.repository

import com.librarys.ferreira.core.domain.model.model.AccountInfo

interface AccountRepository {

    /**
     * Adiciona os dados de uma nova conta de mesa proprietária no banco de dados
     * @param accountInfo Dados da conta
     * @return true se a conta foi salva com sucesso, false caso contrário
     */
    suspend fun saveAccountInDb(accountInfo: AccountInfo) : Boolean

}