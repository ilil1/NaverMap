package com.project.navermap.presentation.mainActivity.myinfo.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.navermap.MapApplication.Companion.appConfig
import com.project.navermap.R
import com.project.navermap.presentation.mainActivity.myinfo.MyInfoViewModel
import com.project.navermap.presentation.ui.extensions.dpToSp


@Composable
fun MyInfoProfile(
    viewModel: MyInfoViewModel,
    modifier: Modifier = Modifier,
    showClickBtn: Boolean,
    onClickNickSaveBtn: () -> Unit
) {

    val nickName = appConfig.getNickName()
    // 이미지 저장 로직을 최상단으로 이동 예정
//    val appCnt = LocalContext.current.applicationContext as Application
//    var imageUri by remember { mutableStateOf<Uri?>(null) }
//
//    val imageLauncher =
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
//            if (uri != null) {
//                saveImageToSharedPreferences(appCnt, uri)
//            }
//        }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        imageUri = getImageUriFromSharedPreferences(appCnt)

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
                .size(100.dp)
                .clip(CircleShape)
                .clickable {
//                    imageLauncher.launch("image/*")
                }
        )
//        }

        Text(
            text = appConfig.getNickName() ?: "",
            fontSize = 15.dpToSp(),
            color = Color.Black,
            modifier = Modifier
                .size(width = 200.dp, height = 60.dp)
                .padding(start = 5.dp)
        )
    }
}