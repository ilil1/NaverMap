package com.project.navermap.presentation.mainActivity.myinfo

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.navermap.R
import com.project.navermap.presentation.ui.extensions.dpToSp
import com.project.navermap.presentation.ui.theme.ColorBase


@Composable
fun MyInfoHomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MyInfoViewModel,
    onClickBackPress: () -> Unit,
    onClickProfileImage: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {

            TopBar(
                modifier = Modifier,
                onBackPress = {}
            )

            Profile(
                modifier = Modifier
                    .fillMaxWidth(),
                userName = "HeeTae"
            )

        }
    }
}


@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    onBackPress : () -> Unit
) {
    val screenWidth = LocalDensity.current.run { LocalConfiguration.current.screenWidthDp.dp }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(ColorBase),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            painter = painterResource(id = R.drawable.left_white),
            contentDescription = "back",
            modifier = Modifier
                .padding(start = 15.dp)
                .size(20.dp)
                .clickable { onBackPress() }
        )

        Text(
            text = "내 정보",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(vertical = 12.dp)
                .padding(start = ((screenWidth / 2) - 60.dp)),
            textAlign = TextAlign.Center,
            fontSize = 20.dpToSp(),
            color = Color.White
        )
    }
}

@Composable
private fun Profile(
    modifier: Modifier = Modifier,
    userName: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "프로필 이미지",
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .size(110.dp)
        )

        Text(
            text = userName,
            textAlign = TextAlign.Center,
            fontSize = 20.dpToSp(),
            color = Color.Black,
            fontStyle = FontStyle.Normal,
            modifier = Modifier.padding(start = 5.dp)
        )


    }

}

