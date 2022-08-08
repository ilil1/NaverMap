package com.project.navermap.presentation.MainActivity.map.mapFragment

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Rect
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import com.project.navermap.databinding.DialogFilterBinding
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint


class FilterDialog(private val context : Activity) {

    lateinit var builder: AlertDialog.Builder
    lateinit var dialog: AlertDialog
    //private lateinit var dialog: Dialog

    private lateinit var chkAll: CheckBox
    private var filterCategoryOptions = mutableListOf<CheckBox>()

    val dialogBinding by lazy {
        val displayRectangle = Rect()
        context.window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        DialogFilterBinding.inflate(context.layoutInflater).apply {
            root.minimumHeight = (displayRectangle.width() * 0.9f).toInt()
            root.minimumHeight = (displayRectangle.height() * 0.9f).toInt()
        }
    }

    fun initDialog(viewModel :MapViewModel) {

        //dialog = Dialog(this)
        //dialog.setCancelable(false)

        builder = AlertDialog.Builder(context)
        builder.setCancelable(false)

//        val dialog = Dialog(context)
//        dialog.setContentView(R.layout.dialog_update)
//        val width = WindowManager.LayoutParams.MATCH_PARENT
//        val height = WindowManager.LayoutParams.WRAP_CONTENT
//        dialog.window!!.setLayout(width, height)
//        dialog.show()

        chkAll = dialogBinding.all

        with(dialogBinding) {
            filterCategoryOptions.addAll(
                arrayOf(
                    foodBeverage, service, fashionAccessories,
                    supermarket, fashionClothes, etc
                )
            )
        }

        chkAll.setOnClickListener {
            filterCategoryOptions.forEach { checkBox ->
                checkBox.isChecked = chkAll.isChecked
            }
        }

        filterCategoryOptions.forEach { checkBox ->
            //filterCategoryChecked.add(true)
            //viewModel.SetCategoryChecked(filterCategoryChecked)// btnclose 할 시 ture 반환을 위해서
            viewModel.filterCategoryChecked.add(true)
            checkBox.setOnClickListener {
                for (_checkBox in filterCategoryOptions) {
                    if (!_checkBox.isChecked) {
                        chkAll.isChecked = false
                        return@setOnClickListener
                    }
                }
                chkAll.isChecked = true
            }
        }

        dialogBinding.btnCloseFilter.setOnClickListener {

            var check = true
            for (i in 0 until filterCategoryOptions.size) {
                //filterCategoryOptions[i].isChecked = viewModel.getCategoryChecked()[i]
                filterCategoryOptions[i].isChecked = viewModel.filterCategoryChecked[i]
                if (!filterCategoryOptions[i].isChecked)
                    check = false
            }
            chkAll.isChecked = check

            dialog.dismiss()
            (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
        }

        dialogBinding.btnFilterReset.setOnClickListener {

            filterCategoryOptions.forEach { it.isChecked = true }

            var check = true
            for (item in filterCategoryOptions)
                if (!item.isChecked) {
                    check = false
                }

            if (check) chkAll.isChecked = true
        }

        dialogBinding.btnFilterApply.setOnClickListener {

            var noChk = true
            for (item in filterCategoryOptions) {
                if (item.isChecked) {
                    noChk = false
                    break
                }
            }

            if (noChk) {
                Toast.makeText(context, "적어도 하나 이상 카테고리를 선택해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            for (i in 0 until filterCategoryOptions.size)
                viewModel.filterCategoryChecked[i] = filterCategoryOptions[i].isChecked

            viewModel.updateMarker()

            dialog.dismiss()
            (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
        }

        //dialog.setContentView(dialogBinding.root)

        builder.setView(dialogBinding.root)
        builder.create()
    }
}