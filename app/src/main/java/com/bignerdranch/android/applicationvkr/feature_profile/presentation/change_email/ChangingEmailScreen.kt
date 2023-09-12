package com.bignerdranch.android.applicationvkr.feature_profile.presentation.change_email

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bignerdranch.android.applicationvkr.R
import com.bignerdranch.android.applicationvkr.core.presentation.asString
import com.bignerdranch.android.applicationvkr.core.presentation.components.StandardTextField
import com.bignerdranch.android.applicationvkr.core.util.UIEvent
import com.bignerdranch.android.applicationvkr.feature_auth.presentation.util.AuthError
import com.bignerdranch.android.applicationvkr.ui.theme.SpaceLarge
import com.bignerdranch.android.applicationvkr.ui.theme.SpaceMedium
import com.bignerdranch.android.applicationvkr.ui.theme.poppinsFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@ExperimentalComposeUiApi
@Composable
fun ChangingEmailScreen(
    onNavigate: () -> Unit = {},
    onNavigateUp: () -> Unit = {},
    onPopBackStack: () -> Unit,
    scaffoldState: ScaffoldState,
    viewModel: ChangingEmailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val emailState = viewModel.emailState.value
    val context = LocalContext.current

    val localFocusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        viewModel.onChangedEmail.collect {
            delay(1000L)
            onPopBackStack()
        }
    }

    LaunchedEffect(key1 = keyboardController) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    keyboardController?.hide()
                    scaffoldState.snackbarHostState.showSnackbar(
                        event.uiText.asString(context),
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.onWastedToken.collect {
            delay(1000L)
            onNavigate()
        }
    }

    // подумать, как сделать так, чтоб не весело просто так

    LaunchedEffect(key1 = true, key2 = true) {
        viewModel.getEmail()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = SpaceLarge,
                end = SpaceLarge,
                top = SpaceLarge,
                bottom = 50.dp
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
        ) {
            Text(
                text = stringResource(id = R.string.changed_email),
                style = MaterialTheme.typography.h3,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            StandardTextField(
                text = emailState.text,
                onValueChange = {
                    viewModel.onEvent(ChangeEmailEvent.EnteredEmail(it))
                },
                error = when (emailState.error) {
                    is AuthError.FieldEmpty -> {
                        stringResource(id = R.string.error_field_empty)
                    }
                    is AuthError.InvalidEmail -> {
                        stringResource(id = R.string.not_a_valid_email)
                    }
                    else -> ""
                },
                style = MaterialTheme.typography.subtitle1
                    .copy(
                        color = Color.White,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.SemiBold
                    ),
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = {
                    localFocusManager.clearFocus()
                }),
                hint = stringResource(id = R.string.email)
            )
            Spacer(modifier = Modifier.height(SpaceLarge))
            Button(
                onClick = {
                    viewModel.onEvent(ChangeEmailEvent.ChangeEmail)
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(45.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.subtitle1
                        .copy(
                            color = Color.White,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.SemiBold
                        ),
                )
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
