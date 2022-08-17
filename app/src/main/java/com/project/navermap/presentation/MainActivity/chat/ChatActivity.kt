package com.project.navermap.presentation.MainActivity.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.navermap.R
import com.project.navermap.databinding.ActivityChatBinding
import com.project.navermap.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}