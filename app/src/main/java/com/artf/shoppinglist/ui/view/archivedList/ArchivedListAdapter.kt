package com.artf.shoppinglist.ui.view.archivedList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artf.shoppinglist.databinding.ItemArchivedShoppingListBinding
import com.artf.shoppinglist.ui.data.model.ShoppingListUi

class ArchivedListAdapter(
    private val clickListener: ClickListener
) : ListAdapter<ShoppingListUi,
        RecyclerView.ViewHolder>(GridViewDiffCallback) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = getItem(position)
        (holder as MsgViewHolder).bind(clickListener, product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgViewHolder {
        return MsgViewHolder(
            ItemArchivedShoppingListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class MsgViewHolder constructor(private val binding: ItemArchivedShoppingListBinding) :
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