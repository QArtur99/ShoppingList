package com.artf.shoppinglist.ui.data

import com.artf.shoppinglist.data.database.model.Product
import com.artf.shoppinglist.ui.data.model.ProductUi
import com.artf.shoppinglist.util.ShoppingListType

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