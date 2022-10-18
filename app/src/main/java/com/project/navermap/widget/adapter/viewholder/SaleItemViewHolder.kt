package com.project.navermap.widget.adapter.viewholder


import androidx.lifecycle.ViewModel
import com.project.navermap.extensions.clear
import com.project.navermap.extensions.load
import com.project.navermap.databinding.ViewholderSaleItemBinding
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.listener.AdapterListener
import com.project.navermap.widget.adapter.listener.StoreDetailItemListener

class SaleItemViewHolder(
    private val binding: ViewholderSaleItemBinding,
    viewModel: ViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<FoodModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        marketImageView.clear()
    }

    override fun bindData(model: FoodModel) {
        super.bindData(model)

//        val disCountedPrice = model.price - model.salePrice
//        val disCountPercent: Int = 100 * disCountedPrice / model.originalPrice

        with(binding) {
            // TODO 실제 데이터를 받아오는 경우 데이터가 잘 반영이 되도록 수정
            marketImageView.load(model.imageUrl)
            itemNameText.text = model.title // data
            originPriceTextView.text = "N/A"
//                resourcesProvider.getString(R.string.home_item_price_format, model.originalPrice)
            disCountPercentTextView.text = "20%"
//                resourcesProvider.getString(
//                R.string.home_item_discount_percent_format,
//                disCountPercent,
//                "%"


            salePriceTextView.text ="N/A"
        }
    }

    override fun bindViews(model: FoodModel, listener: AdapterListener) {
        if(listener is StoreDetailItemListener) {
            with(binding) {
                root.setOnClickListener {
                    listener.onClickItem(model)
                }
            }
        }
    }
}
