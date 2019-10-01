package com.artf.shoppinglist.ui.archivedList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.databinding.ItemArchivedShoppingListBinding

class ArchivedListAdapter(
    private val fragment: Fragment,
    private val clickListenerInt: ClickListenerInt
) : ListAdapter<ShoppingList,
        RecyclerView.ViewHolder>(GridViewDiffCallback) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = getItem(position)
        (holder as MsgViewHolder).bind(fragment, clickListenerInt, product)
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

        fun bind(fragment: Fragment, clickListenerInt: ClickListenerInt, item: ShoppingList) {
            binding.lifecycleOwner = fragment
            binding.item = item
            binding.clickListenerInt = clickListenerInt
            binding.executePendingBindings()
        }
    }

    companion object GridViewDiffCallback : DiffUtil.ItemCallback<ShoppingList>() {
        override fun areItemsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
            return oldItem == newItem
        }
    }

    interface ClickListenerInt {
        fun onClickListenerRow(shoppingList: ShoppingList)
        fun onClickListenerButton(shoppingList: ShoppingList)
    }
}