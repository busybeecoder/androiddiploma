package com.bignerdranch.android.applicationvkr.feature_profile.domain.use_case

data class ProfileUseCases(
    val logout: LogoutUseCase,
    val getEmail: GetEmailUseCase,
    val changeEmail: ChangeEmailUseCase,
    val changePassword: ChangePasswordUseCase
)
