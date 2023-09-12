package com.bignerdranch.android.applicationvkr.feature_profile.presentation.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.* // ktlint-disable no-wildcard-imports
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.bignerdranch.android.applicationvkr.R
import com.bignerdranch.android.applicationvkr.core.presentation.asString
import com.bignerdranch.android.applicationvkr.core.presentation.components.StandardToolbar
import com.bignerdranch.android.applicationvkr.core.util.*
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.GameRequest
import com.bignerdranch.android.applicationvkr.feature_search.presentation.main_page.TextLabelRounded
import com.bignerdranch.android.applicationvkr.ui.theme.*
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@ExperimentalComposeUiApi
@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    scaffoldState: ScaffoldState,
    onLogout: () -> Unit = {},
    onWastedToken: () -> Unit = {},
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val favouriteListState = viewModel.favouriteListState.value
//    val currentPasswordState = viewModel.currentPasswordState.value
//    val newPasswordState = viewModel.newPasswordState.value
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.onWastedToken.collect {
            delay(1000L)
            onWastedToken()
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current

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
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }
                is UIEvent.Navigate -> {
                    onNavigate(event.route)
                }
                is UIEvent.OnLogout -> {
                    onLogout()
                }
            }
        }
    }

    LaunchedEffect(key1 = true, key2 = true) {
        viewModel.getFavorites()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        StandardToolbar(
            title = {
                Text(
                    text = stringResource(id = R.string.profile),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h6
                        .copy(
                            color = Color.White,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.SemiBold,
                        ),
                )
            },
            modifier = Modifier.fillMaxWidth(),
            showBackArrow = false,
            navActions = {
                IconButton(
                    onClick = {
                        viewModel.onEvent(ProfileEvent.Logout)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "",
                        tint = MaterialTheme.colors.onBackground
                    )
                }
            }
        )
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
//                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
            ) {
                Button(
                    onClick = {
                        onNavigate(Screen.ChangePasswordScreen.route)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = LightGray),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .height(45.dp)
                        .clip(RoundedCornerShape(15.dp))
                ) {
                    Text(
                        text = stringResource(id = R.string.changed_password_button),
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.subtitle1
                            .copy(
                                color = Color.White,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.SemiBold
                            ),
                    )
                }
                Spacer(
                    modifier = Modifier.height(15.dp)
                )
                Button(
                    onClick = {
                        onNavigate(Screen.ChangeEmailScreen.route)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = LightGray),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .height(45.dp)
                        .clip(RoundedCornerShape(15.dp))
                ) {
                    Text(
                        text = stringResource(id = R.string.changed_email_button),
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.subtitle1
                            .copy(
                                color = Color.White,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.SemiBold
                            ),
                    )
                }
                if (favouriteListState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    ShowList(favouriteListState, onNavigate)
                }
            }
        }
    }
}

@Composable
fun ShowList(
    favoriteState: FavoriteState,
//    gameList: List<GameRequest>,
    clickOnElement: (String) -> Unit,
) {
    Text(
        text = "Избранное",
        Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 23.sp,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Start,
        color = White,
    )
    if (favoriteState.isEmpty) {
        Text(
            text = "Нет избранных игр",
            Modifier
                .fillMaxWidth(),
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            color = LightGray,
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            items(favoriteState.gameList.size) { gameIndex ->
                ShowFavouritesGame(game = favoriteState.gameList[gameIndex], clickOnElement)
            }
        }
    }
}

@Composable
private fun ShowFavouritesGame(
    game: GameRequest,
    onGameClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = MediumGray)
            .fillMaxWidth()
            .clickable {
                onGameClick(Screen.GameScreen.route + "?gameId=${game.id}")
            }
    ) {
        Row(
//            modifier = Modifier
//                .padding(7.dp)
//                .clip(shape = RoundedCornerShape(15.dp))
        ) {
            Column(
                modifier = Modifier
//                    .background(color = Yellow)
            ) {
                Box {
                    GameCover(url = game.headerImage)
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
//                    .background(Red)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = game.name,
                    Modifier
                        .fillMaxSize(),
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 23.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(
                    bottom = 7.dp
                )
//                .background(Cyan)
                .horizontalScroll(state = rememberScrollState())
        ) {
            Spacer(
                modifier = Modifier.width(15.dp)
            )
            TextLabelRounded(
                text = game.releaseDate
            )
            TextLabelRounded(
                text = game.publisher
            )
        }
        FlowRow(
            modifier = Modifier.padding(
                bottom = 10.dp
            )
//                .background(White)
                .horizontalScroll(state = rememberScrollState())
        ) {
            Spacer(
                modifier = Modifier.width(15.dp)
            )
            game.tags.forEach {
                TextLabelRounded(
                    textColor = GreenAccent,
                    backgroundColor = MediumGray,
                    text = it.replace("_", " ").replaceFirstChar { it.uppercase() },
                    borderColor = DarkerGreen,
                    borderWidth = 1.dp
                )
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun GameCover(url: String?) {
    val painter = rememberImagePainter(
        data = url,
        builder = {
            crossfade(500)
        }
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .padding(start = 15.dp, top = 15.dp, bottom = 7.dp, end = 10.dp)
            .size(width = 125.dp, height = 60.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(Color.Black),
        contentScale = ContentScale.FillHeight

    )
}
