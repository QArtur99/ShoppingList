package com.artf.shoppinglist.model

import com.artf.shoppinglist.util.ShoppingListType

data class ProductUi(
    val id: Long,
    val productName: String,
    val productQuantity: Long,
    val productTimestamp: Long,
    val shoppingListId: Long,
    val shoppingListType: ShoppingListType
)