package com.bignerdranch.android.applicationvkr.core.util

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object HomeScreen : Screen("home_screen")
    object ProfileScreen : Screen("profile_screen")
    object ChangeEmailScreen : Screen("change_email_screen")
    object ChangePasswordScreen : Screen("change_password_screen")
    object GameScreen : Screen("game_screen")
}
