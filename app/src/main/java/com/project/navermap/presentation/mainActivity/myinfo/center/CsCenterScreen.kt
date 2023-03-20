package com.project.navermap.presentation.mainActivity.myinfo.center

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.project.navermap.R

@Composable
fun CsCenterScreen() {
    Surface(color = Color.White) {
        Column(modifier = Modifier.fillMaxSize()) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                val (back, title) = createRefs()
                Image(
                    painter = painterResource(id = R.drawable.left),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .constrainAs(back) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "고객센터",
                    color = MaterialTheme.colors.primary,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(title) {
                        start.linkTo(back.end, margin = 10.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                )
            }

            Box(
                Modifier
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
            )

            CustomerServiceItem(
                iconRes = R.drawable.question,
                text = "자주하는 질문"
            )

            Box(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )

            CustomerServiceItem(
                iconRes = R.drawable.email,
                text = "이메일로 문의하기"
            )

            Box(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )

            CustomerServiceItem(
                iconRes = R.drawable.emer,
                text = "이물질 신고"
            )

            Box(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )

            CustomerServiceItem(
                iconRes = R.drawable.call,
                text = "YU MARKET 고객센터(전화)"
            )

            Box(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )

            CustomerServiceItem(
                iconRes = R.drawable.gohome,
                text = "입점문의"
            )

            Box(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Black)
            ) {
                val (text) = createRefs()
                Text(
                    text = "(주)위드마켓",
                    color = Color.White,
                    fontSize = 15.sp,
                    modifier = Modifier.constrainAs(text) {
                        start.linkTo(parent.start, margin = 15.dp)
                        top.linkTo(parent.top, margin = 5.dp)
                    }
                )
            }
        }
    }
}

@Composable
fun CustomerServiceItem(iconRes: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .padding(start = 15.dp, top = 10.dp, bottom = 10.dp)
        )
        Text(
            text = text,
            color = Color.Black,
            fontSize = 15.sp,
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                .weight(1f)
        )
    }
}



