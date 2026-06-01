package com.librarys.ferreira.core.data.repository

import com.librarys.ferreira.core.data.local.dao.AccountDao
import com.librarys.ferreira.core.data.mapper.toEntity
import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.repository.AccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {

    override suspend fun saveAccountInDb(accountInfo: AccountInfo): Boolean {
        val accountEntity = accountInfo.toEntity()
        return try {
            val registros = accountDao.insertAccount(accountEntity)
            registros > 0
        } catch (e: Exception){
            false
        }
    }
}