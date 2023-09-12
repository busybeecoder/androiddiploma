package com.bignerdranch.android.applicationvkr.feature_search.presentation.game_page

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.bignerdranch.android.applicationvkr.R
import com.bignerdranch.android.applicationvkr.core.util.Constants
import com.bignerdranch.android.applicationvkr.core.util.openUrlInBrowser
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.FullGameRequest
import com.bignerdranch.android.applicationvkr.ui.theme.DarkGray
import com.bignerdranch.android.applicationvkr.ui.theme.GreenAccent
import com.bignerdranch.android.applicationvkr.ui.theme.poppinsFamily

@Composable
fun SteamDiscountLine(
    context: Context,
    game: FullGameRequest,
) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 6.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = DarkGray)
            .fillMaxWidth()
            .clickable {
                context.openUrlInBrowser(Constants.STEAM_LINK + game.prices.steam?.uri)
            }
    ) {
        val painter = rememberImagePainter(
            data = R.drawable.steam_logo,
            builder = {
                crossfade(500)
            }
        )
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 12.dp, top = 6.dp, bottom = 6.dp)
                .size(width = 40.dp, height = 40.dp)
//                        .background(Color.Red)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Fit

        )
        Text(
            text = "Steam",
            Modifier
                .padding(horizontal = 8.dp)
//                        .background(Color.Cyan)
                .align(Alignment.CenterVertically),
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .align(Alignment.CenterVertically)

        ) {
            Row() {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clip(shape = RoundedCornerShape(7.dp))
                        .align(Alignment.CenterVertically)
                        .background(GreenAccent)
                        .padding(5.dp)
                ) {
                    Text(
                        color = DarkGray,
                        text = "-${game.prices.steam?.discount_percent}%"
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                ) {
                    Text(
                        text = game.prices.steam!!.initialPrice,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                    Text(
                        text = game.prices.steam.finalPrice,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
fun SteamPriceLine(
    context: Context,
    game: FullGameRequest,
) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 6.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = DarkGray)
            .fillMaxWidth()
            .clickable {
                context.openUrlInBrowser(Constants.STEAM_LINK + game.prices.steam?.uri)
            }
    ) {
        val painter = rememberImagePainter(
            data = R.drawable.steam_logo,
            builder = {
                crossfade(500)
            }
        )
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 12.dp, top = 6.dp, bottom = 6.dp)
                .size(width = 40.dp, height = 40.dp)
//                        .background(Color.Red)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Fit

        )
        Text(
            text = "Steam",
            Modifier
                .padding(horizontal = 8.dp)
//                        .background(Color.Cyan)
                .align(Alignment.CenterVertically),
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .align(Alignment.CenterVertically)

        ) {
            Row() {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                ) {
                    Text(
                        text = game.prices.steam!!.finalPrice,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SteamFreeLine(
    context: Context,
    game: FullGameRequest,
) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 6.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = DarkGray)
            .fillMaxWidth()
            .clickable {
                context.openUrlInBrowser(Constants.STEAM_LINK + game.prices.steam?.uri)
            }
    ) {
        val painter = rememberImagePainter(
            data = R.drawable.steam_logo,
            builder = {
                crossfade(500)
            }
        )
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 12.dp, top = 6.dp, bottom = 6.dp)
                .size(width = 40.dp, height = 40.dp)
//                        .background(Color.Red)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Fit

        )
        Text(
            text = "Steam",
            Modifier
                .padding(horizontal = 8.dp)
//                        .background(Color.Cyan)
                .align(Alignment.CenterVertically),
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .align(Alignment.CenterVertically)

        ) {
            Row() {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                ) {
                    Text(
                        text = "Бесплатно",
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            color = Color.White,
                        )
                    )
                }
            }
        }
    }
}
