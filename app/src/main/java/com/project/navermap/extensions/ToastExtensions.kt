package com.project.navermap.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment

/* TODO: 2022-08-21 일 12:25, MapFragment가 BaseFragment를 상속받게 한 후, BaseFragment method로 정의 */
fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}