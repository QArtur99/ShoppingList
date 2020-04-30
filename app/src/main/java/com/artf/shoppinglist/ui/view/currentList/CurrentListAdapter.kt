package com.artf.shoppinglist.ui.view.currentList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artf.shoppinglist.databinding.ItemCurrentShoppingListBinding
import com.artf.shoppinglist.ui.data.model.ShoppingListUi

class CurrentListAdapter(
    private val clickListener: ClickListener
) : ListAdapter<ShoppingListUi,
        RecyclerView.ViewHolder>(GridViewDiffCallback) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = getItem(position)
        (holder as MsgViewHolder).bind(clickListener, product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgViewHolder {
        return MsgViewHolder(
            ItemCurrentShoppingListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class MsgViewHolder constructor(private val binding: ItemCurrentShoppingListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: ClickListener, item: ShoppingListUi) {
            binding.item = item
            binding.clickListenerInt = clickListener
            binding.executePendingBindings()
        }
    }

    companion object GridViewDiffCallback : DiffUtil.ItemCallback<ShoppingListUi>() {
        override fun areItemsTheSame(oldItem: ShoppingListUi, newItem: ShoppingListUi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingListUi, newItem: ShoppingListUi): Boolean {
            return oldItem == newItem
        }
    }

    interface ClickListener {
        fun onClickListenerRow(shoppingList: ShoppingListUi)
        fun onClickListenerButton(shoppingList: ShoppingListUi)
    }
}