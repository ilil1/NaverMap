package com.project.navermap.presentation.mainActivity.myinfo

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.navermap.R
import com.project.navermap.presentation.ui.extensions.dpToSp
import com.project.navermap.presentation.ui.theme.ColorBase


@Composable
fun MyInfo(
    modifier: Modifier = Modifier,
    viewModel: MyInfoViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            topBar(
                modifier = Modifier
            )
        }
    }
}


@Composable
private fun topBar(
    modifier: Modifier = Modifier,
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
        )

        Text(
            text = "내 정보",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(vertical = 12.dp)
                .padding(start = ( (screenWidth/2) - 60.dp) ),
            textAlign = TextAlign.Center,
            fontSize = 20.dpToSp(),
            color = Color.White
        )
    }

}