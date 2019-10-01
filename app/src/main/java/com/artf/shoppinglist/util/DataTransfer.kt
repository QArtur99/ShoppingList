package com.artf.shoppinglist.util

import com.artf.shoppinglist.database.Product
import com.artf.shoppinglist.model.ProductUi

fun List<Product>.asUiModel(shoppingListType: ShoppingListType): List<ProductUi> {
    return map { it.asUiModel(shoppingListType) }
}

fun Product.asUiModel(shoppingListType: ShoppingListType): ProductUi {
    return ProductUi(
        id = id,
        productName = productName,
        productQuantity = productQuantity,
        productTimestamp = productTimestamp,
        shoppingListId = shoppingListId,
        shoppingListType = shoppingListType
    )
}

fun ProductUi.asDbModel(): Product {
    return Product(
        id = id,
        productName = productName,
        productQuantity = productQuantity,
        productTimestamp = productTimestamp,
        shoppingListId = shoppingListId
    )
}