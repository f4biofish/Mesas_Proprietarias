package com.librarys.ferreira.core.domain.usecase.account

import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.repository.AccountRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(accountInfo: AccountInfo): Boolean {
        return repository.deleteAccount(accountInfo)
    }
}
