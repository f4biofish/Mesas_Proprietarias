package com.librarys.ferreira.core.domain.usecase.account

import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.repository.AccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(account: AccountInfo) : Result<Unit> {
        val flag = accountRepository.saveAccountInDb(account)
        return if(flag) Result.success(Unit) else Result.failure(Exception("Erro ao salvar a conta"))
    }

}