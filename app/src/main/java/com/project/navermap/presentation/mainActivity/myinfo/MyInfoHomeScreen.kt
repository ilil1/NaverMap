package com.project.navermap.presentation.mainActivity.myinfo

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.navermap.MapApplication.Companion.appConfig
import com.project.navermap.R
import com.project.navermap.presentation.mainActivity.myinfo.custom.MyInfoProfile
import com.project.navermap.presentation.mainActivity.myinfo.custom.UserSectionItem
import com.project.navermap.presentation.ui.extensions.dpToSp
import com.project.navermap.presentation.ui.theme.ColorBase
import com.project.navermap.presentation.ui.theme.ColorC1C1C1


@Composable
fun MyInfoHomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MyInfoViewModel,
    onClickBackPress: () -> Unit,
    onClickProfileImage: () -> Unit,
    onClickClose: () -> Unit
) {
    val nickName by remember {
        mutableStateOf(appConfig.getNickName())
    }
    val appCnt = LocalContext.current.applicationContext as Application
    val showClickBtn = viewModel.showSaveBtn.collectAsState().value


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
                onClickActivityBackPress = { onClickClose() }
            )

            MyInfoProfile(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth(),
                showClickBtn = showClickBtn,
                onClickNickSaveBtn = { viewModel.saveNickname(appCnt, appConfig) }
            )

            Divider(
                modifier = Modifier,
                color = ColorC1C1C1,
                thickness = 13.dp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                UserSectionItem(
                    modifier = Modifier
                        .weight(1f),
                    onClickOrderItem = {},
                    onClickReviewItem = {},
                    onClickFavoriteItem = {},
                    onClickInterestingItem = {},
                )
            }

            Divider(
                modifier = Modifier,
                color = ColorC1C1C1,
                thickness = 10.dp
            )

            Column {
                Section(title = "계정", items = listOf("주소 재설정", "로그아웃"))
                Divider(color = Color.Gray, thickness = 3.dp)
                Section(title = "앱 설정", items = listOf("알림설정", "환경설정"))
                Divider(color = Color.Gray, thickness = 3.dp)
                Section(title = "이용 안내", items = listOf("고객센터", "이용약관", "개인정보 처리 방침"))
            }
        }
    }


}


@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    onClickActivityBackPress: () -> Unit
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
                .clickable { onClickActivityBackPress() }
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
fun Section(title: String, items: List<String>) {
    Column(modifier = Modifier.padding(horizontal = 18.dp)) {
        Text(
            text = title,
            fontSize = 30.dpToSp(),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        items.forEach { item ->
            Text(
                text = item,
                fontSize = 15.dpToSp(),
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
        }
    }
}


private fun saveImageToSharedPreferences(application: Application, imageUri: Uri) {
    // SharedPreferences 인스턴스 가져오기
    val sharedPreferences = application.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    // 이미지 Uri를 문자열로 변환하여 SharedPreferences에 저장
    val editor = sharedPreferences.edit()
    editor.putString("imageUri", imageUri.toString())
    editor.apply()
}

// 이미지 Uri를 가져오는 함수
private fun getImageUriFromSharedPreferences(application: Application): Uri? {
    // SharedPreferences 인스턴스 가져오기
    val sharedPreferences = application.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    // 저장된 이미지 Uri를 가져와서 Uri 객체로 변환하여 반환
    val imageUriString = sharedPreferences.getString("imageUri", null)
    return imageUriString?.let { Uri.parse(it) }
}





