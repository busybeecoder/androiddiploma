package com.bignerdranch.android.applicationvkr.feature_profile.domain.use_case

import com.bignerdranch.android.applicationvkr.core.util.Resource
import com.bignerdranch.android.applicationvkr.feature_profile.data.remote.ProfileEmailRequest
import com.bignerdranch.android.applicationvkr.feature_profile.domain.repository.ProfileRepository

class GetEmailUseCase(
    private val repository: ProfileRepository
) {

    suspend operator fun invoke(): Resource<ProfileEmailRequest> {
        return repository.getEmail()
    }
}
