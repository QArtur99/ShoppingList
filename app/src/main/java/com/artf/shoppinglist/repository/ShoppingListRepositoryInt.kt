package com.artf.shoppinglist.repository

import androidx.lifecycle.LiveData
import com.artf.shoppinglist.database.Product
import com.artf.shoppinglist.database.ShoppingList

interface ShoppingListRepositoryInt {
    suspend fun updateShoppingList(shoppingList: ShoppingList)

    suspend fun updateShoppingListItem(product: Product)

    suspend fun insertShoppingList(shoppingList: ShoppingList)

    suspend fun insertProduct(product: Product)

    suspend fun deleteProduct(product: Product)

    fun getCurrentShoppingList(): LiveData<List<ShoppingList>>

    fun getArchivedShoppingList(): LiveData<List<ShoppingList>>

    fun getAllShoppingListItem(listId: Long): LiveData<List<Product>>
}