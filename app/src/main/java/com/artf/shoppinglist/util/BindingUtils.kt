package com.artf.shoppinglist.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.model.ProductUi
import com.artf.shoppinglist.ui.archivedList.ArchivedListAdapter
import com.artf.shoppinglist.ui.currentList.CurrentListAdapter
import com.artf.shoppinglist.ui.detailList.ProductListAdapter
import java.util.*

@BindingAdapter("currentLists")
fun bindCurrentLists(recyclerView: RecyclerView, data: List<ShoppingList>?) {
    val adapter = recyclerView.adapter as CurrentListAdapter
    adapter.submitList(data)
    adapter.notifyDataSetChanged()
}

@BindingAdapter("archivedLists")
fun bindArchivedList(recyclerView: RecyclerView, data: List<ShoppingList>?) {
    val adapter = recyclerView.adapter as ArchivedListAdapter
    adapter.submitList(data)
    adapter.notifyDataSetChanged()
}

@BindingAdapter("productList")
fun bindProductList(recyclerView: RecyclerView, data: List<ProductUi>?) {
    val adapter = recyclerView.adapter as ProductListAdapter
    adapter.submitList(data)
    adapter.notifyDataSetChanged()
}


@BindingAdapter("shoppingListTimestamp")
fun bindShoppingListTimestamp(textView: TextView, timestampLong: Long) {
    val text = getDateFormat().format(Date(timestampLong))
    textView.text = text
}

@BindingAdapter("productQuantity")
fun bindProductQuantity(textView: TextView, long: Long) {
    val text = if (1 > long) "" else long.toString()
    textView.text = text
}

@BindingAdapter("listType")
fun bindListType(view: View, shoppingListType: ShoppingListType?) {
    view.visibility = when (shoppingListType) {
        ShoppingListType.CURRENT -> View.VISIBLE
        ShoppingListType.ARCHIVED -> View.GONE
        else -> {View.VISIBLE}
    }
}