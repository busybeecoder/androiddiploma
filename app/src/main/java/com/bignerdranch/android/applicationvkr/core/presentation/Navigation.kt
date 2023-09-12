package com.bignerdranch.android.applicationvkr.core.presentation

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bignerdranch.android.applicationvkr.core.util.Constants
import com.bignerdranch.android.applicationvkr.core.util.Screen
import com.bignerdranch.android.applicationvkr.feature_auth.presentation.login.LoginScreen
import com.bignerdranch.android.applicationvkr.feature_auth.presentation.register.RegisterScreen
import com.bignerdranch.android.applicationvkr.feature_profile.presentation.change_email.ChangingEmailScreen
import com.bignerdranch.android.applicationvkr.feature_profile.presentation.change_password.ChangingPasswordScreen
import com.bignerdranch.android.applicationvkr.feature_profile.presentation.profile.ProfileScreen
import com.bignerdranch.android.applicationvkr.feature_search.presentation.game_page.GameScreen
import com.bignerdranch.android.applicationvkr.feature_search.presentation.main_page.HomeScreen

@RequiresApi(Build.VERSION_CODES.N)
@Composable
@ExperimentalComposeUiApi
fun Navigation(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    sharedPreferences: SharedPreferences
) {
    NavHost(
        navController = navController,
        startDestination =
        if (sharedPreferences.getString(
                Constants.KEY_JWT_TOKEN, null
            ) != null
        ) {
//            viewModel.checkToken()
            Screen.HomeScreen.route
        } else Screen.LoginScreen.route
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                onNavigate = navController::navigate,
                onLogin = {
                    navController.popBackStack(
                        route = Screen.LoginScreen.route,
                        inclusive = true
                    )
                    navController.navigate(route = Screen.HomeScreen.route)
                },
                scaffoldState = scaffoldState
            )
        }
        composable(Screen.RegisterScreen.route) {
            RegisterScreen(
                navController = navController,
                scaffoldState = scaffoldState,
                onPopBackStack = navController::popBackStack
            )
        }
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                onNavigate = navController::navigate, onWastedToken = {
                    navController.popBackStack(
                        route = Screen.ChangePasswordScreen.route,
                        inclusive = true
                    )
                    navController.clearBackStack(route = Screen.ChangePasswordScreen.route)
                    navController.navigate(route = Screen.LoginScreen.route) {
                        popUpTo(0)
                    }
                },
                scaffoldState = scaffoldState
            )
        }
        composable(
            route = Screen.GameScreen.route + "?gameId={gameId}",
            arguments = listOf(
                navArgument(name = "gameId") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) {
            it.arguments?.getInt("gameId")?.let { it1 ->
                GameScreen(
                    gameId = it1,
                    scaffoldState = scaffoldState
                )
            }
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(
                // повторить подобный метод для других в случае ошибки 403
                onLogout = {
                    navController.popBackStack(
                        route = Screen.ProfileScreen.route,
                        inclusive = true
                    )
                    navController.navigate(route = Screen.LoginScreen.route)
                },
                onNavigate = navController::navigate,
                onWastedToken = {
                    navController.popBackStack(
                        route = Screen.ChangeEmailScreen.route,
                        inclusive = true
                    )
                    navController.clearBackStack(route = Screen.ChangeEmailScreen.route)
                    navController.navigate(route = Screen.LoginScreen.route) {
                        popUpTo(0)
                    }
                },
                scaffoldState = scaffoldState,
            )
        }
        composable(Screen.ChangeEmailScreen.route) {
            ChangingEmailScreen(
                scaffoldState = scaffoldState,
                onPopBackStack = navController::popBackStack,
                onNavigate = {
                    navController.popBackStack(
                        route = Screen.ChangeEmailScreen.route,
                        inclusive = true
                    )
                    navController.clearBackStack(route = Screen.ChangeEmailScreen.route)
                    navController.navigate(route = Screen.LoginScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(Screen.ChangePasswordScreen.route) {
            ChangingPasswordScreen(
                scaffoldState = scaffoldState,
                onPopBackStack = navController::popBackStack,
                onNavigate = {
                    navController.popBackStack(
                        route = Screen.ChangePasswordScreen.route,
                        inclusive = true
                    )
                    navController.clearBackStack(route = Screen.ChangePasswordScreen.route)
                    navController.navigate(route = Screen.LoginScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}
