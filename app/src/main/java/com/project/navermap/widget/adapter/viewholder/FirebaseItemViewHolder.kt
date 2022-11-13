package com.project.navermap.widget.adapter.viewholder


import androidx.lifecycle.ViewModel
import com.project.navermap.R
import com.project.navermap.databinding.ViewholderFirebaseItemBinding
import com.project.navermap.extensions.clear
import com.project.navermap.extensions.load
import com.project.navermap.databinding.ViewholderSaleItemBinding
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.model.ItemModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener
import com.project.navermap.widget.adapter.listener.FirebaseItemListener
import com.project.navermap.widget.adapter.listener.StoreDetailItemListener

class FirebaseItemViewHolder(
    private val binding: ViewholderFirebaseItemBinding,
    viewModel: ViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<ItemModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        marketImageView.clear()
    }

    override fun bindData(model: ItemModel) {
        super.bindData(model)

//        val disCountedPrice = model.price - model.salePrice
//        val disCountPercent: Int = 100 * disCountedPrice / model.originalPrice

        with(binding) {
            // TODO 실제 데이터를 받아오는 경우 데이터가 잘 반영이 되도록 수정
            model.imageUrl?.let { marketImageView.load(it) }
            itemNameText.text = model.name // data
            originPriceTextView.text = model.originalPrice.toString()
//                resourcesProvider.getString(R.string.home_item_price_format, model.originalPrice)
            disCountPercentTextView.text = model.discountedPrice.toString()
//                resourcesProvider.getString(
//                R.string.home_item_discount_percent_format,
//                disCountPercent,
//                "%"


            salePriceTextView.text = model.discountedPrice.toString()
        }
    }

    override fun bindViews(model: ItemModel, listener: AdapterListener) {
        if(listener is FirebaseItemListener) {
            with(binding) {
                root.setOnClickListener {
                    listener.onClickItem(model)
                }
            }
        }
    }
}
