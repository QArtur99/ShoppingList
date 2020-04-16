package com.artf.shoppinglist.util

import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artf.shoppinglist.R
import com.artf.shoppinglist.data.database.model.ShoppingList
import com.artf.shoppinglist.ui.data.model.ProductUi
import com.artf.shoppinglist.ui.view.archivedList.ArchivedListAdapter
import com.artf.shoppinglist.ui.view.currentList.CurrentListAdapter
import com.artf.shoppinglist.ui.view.detailList.ProductListAdapter
import java.util.Date

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
        else -> { View.VISIBLE }
    }
}

@BindingAdapter("bgColor")
fun bindBgColor(view: View, empty: Boolean) {
    val color = if (empty) R.color.colorWhite else R.color.colorBackground
    view.setBackgroundColor(ActivityCompat.getColor(view.context, color))
}
