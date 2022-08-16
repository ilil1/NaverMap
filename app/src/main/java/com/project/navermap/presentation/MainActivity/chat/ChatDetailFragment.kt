package com.project.navermap.presentation.MainActivity.chat

import androidx.navigation.Navigation
import com.project.navermap.R
import com.project.navermap.data.entity.ChatEntity
import com.project.navermap.databinding.FragmentChatDetailBinding
import com.project.navermap.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatDetailFragment : BaseFragment<FragmentChatDetailBinding>() {
    override fun getViewBinding(): FragmentChatDetailBinding =
        FragmentChatDetailBinding.inflate(layoutInflater)

    override fun observeData () {}

    override fun initViews() {
        super.initViews()

        val chatData = arguments?.getParcelable<ChatEntity>("data")

        binding.storeName.text = chatData?.storeName.toString()

        binding.back.setOnClickListener {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_chatDetailFragment_to_chatFragment)
            }
        }
    }
}