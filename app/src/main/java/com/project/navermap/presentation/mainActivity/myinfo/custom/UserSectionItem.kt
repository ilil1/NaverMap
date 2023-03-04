package com.project.navermap.presentation.mainActivity.myinfo.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.navermap.R
import com.project.navermap.presentation.ui.extensions.dpToSp
import com.project.navermap.presentation.ui.theme.ColorC1C1C1

@Composable
fun UserSectionItem(
    modifier: Modifier = Modifier,
    onClickItem: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OrderList(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        ReviewList(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        FavoriteList(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        InterestedList(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun OrderList(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.orderlist),
            contentDescription = "orderList",
            modifier = Modifier.size(50.dp)
        )
        Text(
            text = "주문 목록",
            color = Color.Black,
            fontSize = 12.dpToSp()
        )
    }

}

@Composable
fun ReviewList(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.review),
            contentDescription = "orderList",
            modifier = Modifier.size(50.dp)
        )
        Text(
            text = "리뷰 관리",
            color = Color.Black,
            fontSize = 12.dpToSp()
        )
    }
}

@Composable
fun FavoriteList(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.heart),
            contentDescription = "orderList",
            modifier = Modifier.size(50.dp)
        )
        Text(
            text = "찜 목록",
            color = Color.Black,
            fontSize = 12.dpToSp()
        )
    }
}


@Composable
fun InterestedList(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.category),
            contentDescription = "orderList",
            modifier = Modifier.size(50.dp)
        )
        Text(
            text = "관심 카테고리",
            color = Color.Black,
            fontSize = 12.dpToSp()
        )
    }
}