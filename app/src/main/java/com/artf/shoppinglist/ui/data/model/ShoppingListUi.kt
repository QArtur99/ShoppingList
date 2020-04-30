package com.artf.shoppinglist.ui.data.model

data class ShoppingListUi(
    val id: Long,
    val shoppingListName: String,
    val shoppingListTimestamp: Long,
    var isArchived: Boolean
)