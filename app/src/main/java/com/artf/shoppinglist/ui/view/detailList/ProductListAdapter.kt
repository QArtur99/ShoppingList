package com.artf.shoppinglist.ui.view.detailList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artf.shoppinglist.databinding.ItemProductBinding
import com.artf.shoppinglist.ui.data.model.ProductUi

class ProductListAdapter(
    private val clickListener: ClickListener
) : ListAdapter<ProductUi,
        RecyclerView.ViewHolder>(GridViewDiffCallback) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = getItem(position)
        (holder as MsgViewHolder).bind(clickListener, product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgViewHolder {
        return MsgViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class MsgViewHolder constructor(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: ClickListener, item: ProductUi) {
            binding.item = item
            binding.clickListenerInt = clickListener
            binding.executePendingBindings()
        }
    }

    companion object GridViewDiffCallback : DiffUtil.ItemCallback<ProductUi>() {
        override fun areItemsTheSame(oldItem: ProductUi, newItem: ProductUi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUi, newItem: ProductUi): Boolean {
            return oldItem == newItem
        }
    }

    interface ClickListener {
        fun onClickListenerRow(product: ProductUi)
        fun onClickListenerButton(product: ProductUi)
    }
}