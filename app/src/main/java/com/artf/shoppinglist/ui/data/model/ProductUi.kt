package com.artf.shoppinglist.ui.data.model

import com.artf.shoppinglist.testing.OpenForTesting
import com.artf.shoppinglist.util.ShoppingListType

@OpenForTesting
data class ProductUi(
    val id: Long,
    val productName: String,
    val productQuantity: Long,
    val productTimestamp: Long,
    val shoppingListId: Long,
    val shoppingListType: ShoppingListType
)