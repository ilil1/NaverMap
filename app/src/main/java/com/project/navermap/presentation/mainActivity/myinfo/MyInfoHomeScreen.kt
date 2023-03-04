package com.project.navermap.presentation.mainActivity.myinfo

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.project.navermap.R
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
    onBackActivity: () -> Unit
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
                onClickActivityBackPress = { onBackActivity() }
            )

            Profile(
                modifier = Modifier
                    .fillMaxWidth(),
                userName = "HeeTae"
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
                    onClickItem = {}
                )

            }

            Divider(
                modifier = Modifier,
                color = ColorC1C1C1,
                thickness = 10.dp
            )
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
private fun Profile(
    modifier: Modifier = Modifier,
    userName: String,
) {
    val appCnt = LocalContext.current.applicationContext as Application
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                saveImageToSharedPreferences(appCnt, uri)
            }
        }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        imageUri = getImageUriFromSharedPreferences(appCnt)

//        if (imageUri != null) {
//            Image(
//                painter = rememberAsyncImagePainter(model = imageUri),
//                contentDescription = "Image",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(110.dp)
//                    .clip(CircleShape)
//                    .clickable {
//                        imageLauncher.launch("image/*")
//                    }
//            )
//        } else {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .size(110.dp)
                    .clip(CircleShape)
                    .clickable {
                        imageLauncher.launch("image/*")
                    }
            )
//        }

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





