package com.artf.shoppinglist.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artf.shoppinglist.ui.currentList.CurrentListAdapter
import com.artf.shoppinglist.database.ShoppingList

@BindingAdapter("shoppingLists")
fun bindUserSearchRecyclerView(recyclerView: RecyclerView, data: List<ShoppingList>?) {
    val adapter = recyclerView.adapter as CurrentListAdapter
    adapter.submitList(data)
    adapter.notifyDataSetChanged()
}


