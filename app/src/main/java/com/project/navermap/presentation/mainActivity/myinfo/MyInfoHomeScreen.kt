package com.project.navermap.presentation.mainActivity.myinfo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
    onBackPress: () -> Unit
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
    userName: String,
) {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val imageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (imageUri != null) {
            bitmap.value?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp)
                        .clickable {
                            imageLauncher.launch("image/*")
                            ImageConverter(imageUri!!, context = context)
                        }
                )
            }
        } else {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .size(110.dp)
                    .clickable {
                        imageLauncher.launch("image/*")
                        ImageConverter(imageUri!!, context = context)
                    }
            )
        }

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

private fun ImageConverter(imageUri: Uri, context: Context): Bitmap? {

    var bitmap: Bitmap? = null

    imageUri?.let { imageUri ->
        if (Build.VERSION.SDK_INT < 28) {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }else{
            val source = ImageDecoder.createSource(context.contentResolver,imageUri)
            bitmap = ImageDecoder.decodeBitmap(source)
        }
    }
    return bitmap
}


