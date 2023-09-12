package com.bignerdranch.android.applicationvkr.feature_search.presentation.game_page

import android.content.Context
import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.bignerdranch.android.applicationvkr.core.presentation.asString
import com.bignerdranch.android.applicationvkr.core.util.UIEvent
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.FullGameRequest
import com.bignerdranch.android.applicationvkr.feature_search.presentation.main_page.TextLabelRounded
import com.bignerdranch.android.applicationvkr.ui.theme.*
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalComposeUiApi
@Composable
fun GameScreen(
    gameId: Int,
    viewModel: GameScreenViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
) {

    val state = viewModel.state.value
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.getGame(gameId)
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }
            }
        }
    }
    state.fullGameRequest?.let { ShowFullGame(it, viewModel, context) }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun FullGameCover(url: String?) {
    val painter = rememberImagePainter(
        data = url
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp, end = 10.dp)
            .size(width = 375.dp, height = 180.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(Color.Black),
        contentScale = ContentScale.FillHeight

    )
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
private fun ShowFullGame(
    game: FullGameRequest,
    viewModel: GameScreenViewModel,
    context: Context
) {
    val favouriteState = viewModel.favouriteState.value

    Column(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = MediumGray)
        ) {
            Row(
                modifier = Modifier
//                .padding(7.dp)
//                .clip(shape = RoundedCornerShape(15.dp))
            ) {
                Box {
                    FullGameCover(url = game.headerImage)
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
//                .background(Color.Red)
//                .padding(bottom = 10.dp)
            ) {
                Text(
                    text = game.name,
                    Modifier
//                    .padding(start = 15.dp)
//                    .background(Color.Cyan)
                        .fillMaxWidth(fraction = 0.7f),
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 23.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(
                        onClick = {
                            if (!game.isFavourite) {
                                viewModel.addFavourite(game.id)
                            } else {
                                viewModel.removeFavourite(game.id)
                            }
                        },
                        modifier = Modifier
//                        .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = if (!game.isFavourite) Icons.Filled.FavoriteBorder else Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(
                        bottom = 10.dp
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
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(color = DarkGray)
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {

                Text(
                    text = Html.fromHtml(game.description, Html.FROM_HTML_MODE_COMPACT).toString(),
//                text = game.description,
                    Modifier
                        .fillMaxWidth(),
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
//                maxLines = 2
                )
            }

//            Row(
//                modifier = Modifier
//                    .padding(start = 10.dp, end = 10.dp, bottom = 6.dp)
//                    .clip(shape = RoundedCornerShape(15.dp))
//                    .background(color = DarkGray)
//                    .fillMaxWidth()
//                    .clickable {
//                        context.openUrlInBrowser("http://www.google.com")
//                    }
//            ) {
//                val painter = rememberImagePainter(
//                    data = R.drawable.epic_games_logo,
//                    builder = {
//                        crossfade(500)
//                    }
//                )
//                Image(
//                    painter = painter,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .padding(start = 12.dp, top = 6.dp, bottom = 6.dp)
//                        .size(width = 40.dp, height = 40.dp)
// //                        .background(Color.Red)
//                        .align(Alignment.CenterVertically),
//                    contentScale = ContentScale.Fit
//
//                )
//                Text(
//                    text = "Epic Games",
//                    Modifier
//                        .padding(horizontal = 8.dp)
// //                        .background(Color.Cyan)
//                        .align(Alignment.CenterVertically),
//                    fontFamily = poppinsFamily,
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 19.sp,
//                    overflow = TextOverflow.Ellipsis,
//                    textAlign = TextAlign.Start,
//                )
//                Spacer(modifier = Modifier.weight(1f))
//            }
            when {
                game.prices.steam?.finalPrice == "" -> {
                    SteamFreeLine(context, game)
                }
                game.prices.steam?.discount_percent != 0 -> {
                    SteamDiscountLine(context, game)
                }
                game.prices.steam.finalPrice != "" -> {
                    SteamPriceLine(context, game)
                }
            }

            if (game.prices.egs != null) {
                when {
                    game.prices.egs.finalPrice == "" -> {
                        EgsFreeLine(context, game)
                    }
                    game.prices.egs.discount_percent != 0 -> {
                        EgsDiscountLine(context, game)
                    }
                    game.prices.egs.finalPrice != "" -> {
                        EgsPriceLine(context, game)
                    }
                }
            }

            if (game.prices.gog != null) {
                when {
                    game.prices.gog.finalPrice == "" -> {
                        GogFreeLine(context, game)
                    }
                    game.prices.gog.discount_percent != 0 -> {
                        GogDiscountLine(context, game)
                    }
                    game.prices.gog.finalPrice != "" -> {
                        GogPriceLine(context, game)
                    }
                }
            }

//
//            Row(
//                modifier = Modifier
//                    .padding(start = 10.dp, end = 10.dp, bottom = 6.dp)
//                    .clip(shape = RoundedCornerShape(15.dp))
//                    .background(color = DarkGray)
//                    .fillMaxWidth()
//                    .clickable {
//                        context.openUrlInBrowser(Constants.GOG_LINK + "worms_revolution")
//                    }
//            ) {
//                val painter = rememberImagePainter(
//                    data = R.drawable.gog_logo,
//                    builder = {
//                        crossfade(500)
//                    }
//                )
//                Image(
//                    painter = painter,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .padding(start = 12.dp, top = 6.dp, bottom = 6.dp)
//                        .size(width = 40.dp, height = 40.dp)
// //                        .background(Color.Red)
//                        .align(Alignment.CenterVertically),
//                    contentScale = ContentScale.Fit
//
//                )
//                Text(
//                    text = "GoG",
//                    Modifier
//                        .padding(horizontal = 8.dp)
// //                        .background(Color.Cyan)
//                        .align(Alignment.CenterVertically),
//                    fontFamily = poppinsFamily,
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 19.sp,
//                    overflow = TextOverflow.Ellipsis,
//                    textAlign = TextAlign.Start,
//                )
//                Spacer(modifier = Modifier.weight(1f))
//                Box(
//                    modifier = Modifier
//                        .padding(horizontal = 6.dp)
//                        .align(Alignment.CenterVertically)
//
//                ) {
//                    Row() {
//                        Column(
//                            modifier = Modifier
//                                .align(Alignment.CenterVertically),
//                        ) {
//                            Text(
//                                text = "4250 руб.",
//                                overflow = TextOverflow.Ellipsis,
//                                style = TextStyle(
//                                    fontFamily = poppinsFamily,
//                                    fontWeight = FontWeight.Medium,
//                                    fontSize = 18.sp,
//                                    color = Color.White
//                                )
//                            )
//                        }
//                    }
//                }
//            }
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}
