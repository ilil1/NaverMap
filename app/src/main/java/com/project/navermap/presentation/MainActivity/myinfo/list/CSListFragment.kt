package com.project.navermap.presentation.MainActivity.myinfo.list

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.navermap.R
import com.project.navermap.databinding.FragmentCsListBinding
import com.project.navermap.domain.model.CSModel
import com.project.navermap.domain.model.ImageData
import com.project.navermap.domain.model.category.CSCategory
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.ModelRecyclerAdapter
import com.project.navermap.widget.adapter.listener.CSModelListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CSListFragment : BaseFragment<FragmentCsListBinding>() {


    private val viewModel by viewModels<CSListViewModel>()

    private val csCategory by lazy {
        arguments?.getSerializable(CS_CATEGORY_KEY) as CSCategory
    }


//    private val viewModel by viewModel<CSListViewModel> {
//       // parametersOf(args.csCategory)
//    }
//
//   // private val args by navArgs<CSFragmentArgs>()

    override fun getViewBinding(): FragmentCsListBinding =
        FragmentCsListBinding.inflate(layoutInflater)

    override fun observeData() = with(viewModel) {
        csListData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    private val adapter by lazy {
        ModelRecyclerAdapter<CSModel, CSListViewModel>(
            listOf(), viewModel, resourcesProvider,
            object : CSModelListener {
                override fun onClickItem(listModel: CSModel) {
                    val data = ImageData(listModel.csTitle, listModel.csContentTitle,listModel.csContentBody,listModel.csAuthor)
                    val bundle = Bundle()
                    bundle.putParcelable("data", data)
                    view?.let{
                        backStack()
                    }
                    view?.let { it1 ->
                        Navigation.findNavController(it1)
                            .navigate(R.id.action_CSCenterFragment_to_CSDetailFragment, bundle)
                    }
//                        val intent = Intent(context, CSDetailFragment::class.java).apply {
//                           val data = ImageData(listModel.csTitle,listModel.csContent,listModel.csAuthor)
//                            putExtra(CS_CATEGORY_KEY,data)
//                            putExtra("CSTitle", listModel.csTitle)
//                            putExtra("CSContent", listModel.csContent)
//                            putExtra("CSAuthor", listModel.csAuthor)
//                            putExtra("CSid", listModel.id)
                    //                       }
//                        startActivity(intent)
                }
            }

        )

    }

    override fun initState() = with(viewModel) {
        fetchData()
        super.initState()
    }

    override fun initViews() = with(viewModel) {

        super.initViews()
        binding.csRecyclerView.adapter = adapter
        binding.csRecyclerView.layoutManager = LinearLayoutManager(this@CSListFragment.context)




    }

    companion object {
        const val CS_CATEGORY_KEY = "CSCategoryKey"
        fun newInstance(csCategory: CSCategory): CSListFragment {
            val bundle = Bundle().apply {
                putSerializable(CS_CATEGORY_KEY, csCategory)
            }

            return CSListFragment().apply {
                arguments = bundle
            }
        }
    }

    private fun backStack() {
        view?.let { it1 ->
            Navigation.findNavController(it1).popBackStack()
        }
    }
}