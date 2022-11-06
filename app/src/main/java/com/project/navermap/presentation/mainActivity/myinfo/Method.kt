package com.project.navermap.presentation.mainActivity.myinfo

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class Method()
{
   fun popUp(context: Context) {

       val nowTime = System.currentTimeMillis()
       val date = Date(nowTime)
       val dateAndTime =
           SimpleDateFormat("MM-dd KK:mm ")
       val str_date = dateAndTime.format(date)
       val items = arrayOf("켜기", "끄기")
       var selectionItem: String? = null
       val alertDialog = AlertDialog.Builder(context)
           .setTitle("알림 설정")
           .setSingleChoiceItems(items, 0) { dialog, which ->
               selectionItem = items[which]
               while (selectionItem != null) {
                   if (selectionItem == "켜기") {
                       Toast.makeText(
                           context,
                           "설정 : " + str_date + "부터 알림 켜기를 설정했습니다.",
                           Toast.LENGTH_SHORT
                       )
                           .show()
                       break
                   }
                   if (selectionItem == "끄기") {
                       Toast.makeText(
                           context,
                           "설정 : " + str_date + "부터 알림 끄기를 설정했습니다.",
                           Toast.LENGTH_SHORT
                       )
                           .show()
                       break
                   }
               }
           }
           .setPositiveButton("확인") { dialog, which ->
           }
           .show()
   }
}

