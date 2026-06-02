package com.librarys.ferreira.core.domain.usecase.account

import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountByIdUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    operator fun invoke(id: String): Flow<AccountInfo?> {
        return repository.getAccountById(id)
    }
}
