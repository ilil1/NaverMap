package com.project.navermap.presentation.mainActivity.chat

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.navermap.R
import com.project.navermap.data.entity.ChatEntity
import com.project.navermap.databinding.FragmentChatBinding
import com.project.navermap.domain.model.ChatModel
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.ModelRecyclerAdapter
import com.project.navermap.widget.adapter.listener.ChatModelListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {

    override fun getViewBinding() = FragmentChatBinding.inflate(layoutInflater)

    private val viewModel: ChatViewModel by viewModels()
    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    private val chatAdapter by lazy {
        ModelRecyclerAdapter<ChatModel, ChatViewModel>(
            listOf(), viewModel, resourcesProvider,
            adapterListener = object : ChatModelListener {
                override fun onClickItem(model: ChatModel) {

                    val data = ChatEntity(model.StoreName)
                    val bundle = Bundle()
                    bundle.putParcelable("data", data)

                    view?.let {
                        findNavController()
                            .navigate(R.id.action_chatFragment_to_chatDetailFragment, bundle)
                    }
                }
            }
        )
    }

    override fun initState() {
        viewModel.fetchData()
        binding.chatRecy.adapter = chatAdapter
        super.initState()
    }

    override fun initViews() {
        super.initViews()
        binding.back.setOnClickListener {
            activity?.finish()
        }
    }

    //state로 따로 관리 없이 바로 상태 변화에 따라 adapter에 리스트 반영
    override fun observeData() = with(viewModel) {
        chatListData.observe(viewLifecycleOwner) {
            chatAdapter.submitList(it)
        }
    }
}