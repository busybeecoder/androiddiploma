package com.bignerdranch.android.applicationvkr.feature_search.presentation.main_page

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.bignerdranch.android.applicationvkr.R
import com.bignerdranch.android.applicationvkr.core.presentation.asString
import com.bignerdranch.android.applicationvkr.core.util.Screen
import com.bignerdranch.android.applicationvkr.core.util.UIEvent
import com.bignerdranch.android.applicationvkr.feature_search.data.remote.GameRequest
import com.bignerdranch.android.applicationvkr.ui.theme.*
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@ExperimentalComposeUiApi
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit = {},
    onWastedToken: () -> Unit = {},
    viewModel: HomeScreenViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
) {

//    // под большим вопросом
//    val userInput by viewModel.userInput.observeAsState()

    val queryState = viewModel.queryState.value
    val searchState = viewModel.searchState.value

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true, key2 = true) {
        viewModel.searchGame()
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
            onWastedToken()
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier
                .background(MediumGray)
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .padding(bottom = 7.dp, top = 7.dp)
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(7.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        color = DarkGray,
                        shape = MaterialTheme.shapes.large
                    )
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .weight(16f)
                ) {
                    BasicTextField(
                        value = queryState.text,
                        onValueChange = {
                            viewModel.onEvent(SearchGameEvent.EnteredQuery(it))
                        },
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .fillMaxWidth(),
                        maxLines = 1,
                        cursorBrush = SolidColor(MaterialTheme.colors.primary),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.subtitle1
                            .copy(
                                color = White,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.SemiBold,
                            ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
//                                viewModel.onQueryChanged()
                                keyboardController?.hide()
                            }
                        )
                    )
                }
                Box(
                    modifier = Modifier.weight(2f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(SearchGameEvent.ClearQuery)
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .padding(5.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier.weight(2f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(
                        onClick = {
//                            viewModel.onQueryChanged()
                            viewModel.searchGame()
                            keyboardController?.hide()
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .padding(5.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 7.dp)
            ) {
                val sortHeader = stringResource(R.string.sort_by)
                val sortByDateText = stringResource(R.string.sort_by_date)
                val sortByNameText = stringResource(R.string.sort_by_name)
                var menuHeaderText by remember { mutableStateOf(sortHeader) }
                var icons by remember { mutableStateOf(Icons.Filled.ArrowDropDown) }
                var expanded by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(color = DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { expanded = !expanded }
                            .padding(9.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = menuHeaderText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.subtitle1
                                .copy(
                                    color = White,
                                    fontFamily = poppinsFamily
                                ),
                            modifier = Modifier.padding(end = 8.dp, start = 5.dp)
                        )
                        Icon(imageVector = icons, contentDescription = "")
                        DropdownMenu(
                            modifier = Modifier.fillMaxWidth(fraction = 0.47f),
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    menuHeaderText = sortByDateText
                                    icons = Icons.Filled.ArrowDropDown
                                    viewModel.changeState(HomeScreenViewModel.CHANGE_FROM_DATE_DESCENDING)
                                }
                            ) {
                                Row {
                                    Text(
                                        text = stringResource(R.string.sort_by_date),
                                        style = MaterialTheme.typography.subtitle1
                                            .copy(
                                                color = White,
                                                fontFamily = poppinsFamily,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Icon(
                                        imageVector = Icons.Filled.ArrowDropDown,
                                        contentDescription = ""
                                    )
                                }
                            }
                            DropdownMenuItem(
                                onClick = {
                                    menuHeaderText = sortByDateText
                                    icons = Icons.Filled.ArrowDropUp
                                    viewModel.changeState(HomeScreenViewModel.CHANGE_FROM_DATE_ASCENDING)
                                }
                            ) {
                                Row {
                                    Text(
                                        text = stringResource(R.string.sort_by_date),
                                        style = MaterialTheme.typography.subtitle1
                                            .copy(
                                                color = White,
                                                fontFamily = poppinsFamily,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Icon(
                                        imageVector = Icons.Filled.ArrowDropUp,
                                        contentDescription = ""
                                    )
                                }
                            }
                            DropdownMenuItem(
                                onClick = {
                                    menuHeaderText = sortByNameText
                                    icons = Icons.Filled.ArrowDropDown
                                    viewModel.changeState(HomeScreenViewModel.CHANGE_FROM_NAME_ASCENDING)
//                                    println(viewModel.searchState.value.gameList)
//                                    searchState = viewModel.searchState.value.gameList
                                }
                            ) {
                                Row {
                                    Text(
                                        text = stringResource(R.string.sort_by_name),
                                        style = MaterialTheme.typography.subtitle1
                                            .copy(
                                                color = White,
                                                fontFamily = poppinsFamily,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Icon(
                                        imageVector = Icons.Filled.ArrowDropDown,
                                        contentDescription = ""
                                    )
                                }
                            }
                            DropdownMenuItem(
                                onClick = {
                                    menuHeaderText = sortByNameText
                                    icons = Icons.Filled.ArrowDropUp
                                    viewModel.changeState(HomeScreenViewModel.CHANGE_FROM_NAME_DESCENDING)
//                                    println(viewModel.searchState.value.gameList)
                                }
                            ) {
                                Row() {
                                    Text(
                                        text = stringResource(R.string.sort_by_name),
                                        style = MaterialTheme.typography.subtitle1
                                            .copy(
                                                color = White,
                                                fontFamily = poppinsFamily,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Icon(
                                        imageVector = Icons.Filled.ArrowDropUp,
                                        contentDescription = ""
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(color = DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { viewModel.onEvent(SearchGameEvent.OpenTagDialog) }
                            .padding(9.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.filters),
                            fontSize = 15.sp,
                            modifier = Modifier.padding(end = 8.dp, start = 5.dp),
                            style = MaterialTheme.typography.subtitle1
                                .copy(
                                    color = White,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.SemiBold
                                ),
                        )
                        Icon(imageVector = Icons.Filled.FilterList, contentDescription = "")
                    }
                }
                if (searchState.isTagDialogVisible) {
                    AlertDialog(
                        onDismissRequest = {
                            viewModel.onEvent(SearchGameEvent.CloseTagDialog)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = DarkGray,
                        text = {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Выберите тэги",
                                        fontFamily = poppinsFamily,
                                        style = MaterialTheme.typography.h6,
                                        color = MaterialTheme.colors.onBackground,
                                    )
                                    Spacer(Modifier.weight(1f))
                                    IconButton(
                                        onClick = {
                                            viewModel.onEvent(SearchGameEvent.DismissTagsSelected)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = null,
                                            tint = MaterialTheme.colors.onSurface,
                                        )
                                    }
                                }
                                FlowRow(
                                    mainAxisSpacing = 7.dp,
                                    crossAxisSpacing = 7.dp,
                                ) {
                                    viewModel.tags.value.tags.forEach { tag ->
                                        Chip(
                                            text = tag.replace("_", " ")
                                                .replaceFirstChar { it.uppercase() },
                                            selected = viewModel.tags.value.selectedTags.any { it == tag },
                                            onChipClick = {
                                                viewModel.onEvent(
                                                    SearchGameEvent.SetTagsSelected(
                                                        tag
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        },
                        buttons = {
                            Row(
                                modifier = Modifier.padding(
                                    start = 24.dp,
                                    end = 24.dp,
                                    bottom = 22.dp
                                ),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Spacer(Modifier.weight(1f))
                                Button(
                                    modifier = Modifier.height(35.dp),
                                    onClick = {
                                        viewModel.onEvent(SearchGameEvent.CloseTagDialog)
                                    }
                                ) {
                                    Text(
                                        color = MaterialTheme.colors.onPrimary,
                                        text = "ОК",
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = SpaceMedium,
                    end = SpaceMedium,
                    top = 3.dp,
                    bottom = 58.dp
                )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
            ) {
                if (searchState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    if (searchState.isEmpty) {
                        Text(
                            text = "По вашему запросу ничего не найдено",
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
                        ShowList(searchState.gameList, onNavigate)
                    }
                }
            }
        }
    }
}

@Composable
fun ShowList(
    gameList: List<GameRequest>,
    clickOnElement: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        items(gameList.size) { gameIndex ->
            ShowGame(game = gameList[gameIndex], clickOnElement)
        }
    }
}

@Composable
private fun ShowGame(
    game: GameRequest,
    onGameClick: (String) -> Unit = {},
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
            modifier = Modifier
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
            .background(Black),
        contentScale = ContentScale.FillHeight

    )
}

@Composable
fun TextLabelRounded(
    modifier: Modifier = Modifier,
    text: String,
    borderWidth: Dp = 1.dp,
    fontWeight: FontWeight = FontWeight.SemiBold,
    textColor: Color = White,
    backgroundColor: Color = DarkGray,
    borderColor: Color = DarkGray,
    fontFamily: FontFamily = poppinsFamily
) {
    Text(
        text = text,
        modifier = modifier
            .padding(end = 15.dp)
            .clip(RoundedCornerShape(50.dp))
//                .padding(bottom = 7.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(100.dp)
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 15.dp, vertical = 2.dp),
        color = textColor,
        fontFamily = fontFamily,
        textAlign = TextAlign.Center,
        fontWeight = fontWeight,
        fontSize = 12.sp,
        maxLines = 1
    )
}

@Composable
fun Chip(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    selectedColor: Color = MaterialTheme.colors.primary,
    unselectedColor: Color = MaterialTheme.colors.onSurface,
    onChipClick: () -> Unit
) {
    Text(
        text = text,
        color = if (selected) selectedColor else unselectedColor,
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .border(
                width = 1.dp,
                color = if (selected) selectedColor else unselectedColor,
                shape = RoundedCornerShape(50.dp)
            )
            .clickable {
                onChipClick()
            }
            .padding(SpaceSmall),
    )
}

@Composable
fun ChipTagCard(
    text: String,
    modifier: Modifier = Modifier,
    selectedColor: Color = GreenAccent
) {
    Text(
        text = text,
        color = selectedColor,
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .border(
                width = 2.dp,
                color = selectedColor,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(SpaceSmall),
    )
}
