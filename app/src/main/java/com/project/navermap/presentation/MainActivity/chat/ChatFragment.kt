package com.project.navermap.presentation.MainActivity.chat

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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
import javax.inject.Provider

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private val viewModel: ChatViewModel by viewModels()

    override fun getViewBinding(): FragmentChatBinding =
        FragmentChatBinding.inflate(layoutInflater)

    @Inject
    lateinit var _resourcesProvider: Provider<ResourcesProvider>
    private val resourcesProvider get() = _resourcesProvider.get()

    private val chatadapter by lazy {
        ModelRecyclerAdapter<ChatModel, ChatViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
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

    override fun initState() = with(viewModel) {
        fetchData()
        super.initState()
    }

    override fun initViews() {
        super.initViews()

        binding.chatRecy.adapter = chatadapter
        binding.chatRecy.layoutManager =  GridLayoutManager(
            requireContext(),
            1,
            GridLayoutManager.VERTICAL,
            false
        )

        binding.back.setOnClickListener {
            activity?.finish()
        }
    }

    override fun observeData() = with(viewModel) {
        chatListData.observe(viewLifecycleOwner) {
            chatadapter.submitList(it)
        }
    }
}