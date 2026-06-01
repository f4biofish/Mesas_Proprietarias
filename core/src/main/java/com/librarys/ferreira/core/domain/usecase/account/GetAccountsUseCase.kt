package com.librarys.ferreira.core.domain.usecase.account

import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    operator fun invoke(): Flow<List<AccountInfo>> {
        return repository.getAccounts()
    }
}
