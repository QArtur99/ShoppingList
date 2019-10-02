package com.artf.shoppinglist.repository

import com.artf.shoppinglist.database.Product
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.database.ShoppingListDatabaseDao
import com.artf.shoppinglist.testing.OpenForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@OpenForTesting
@Singleton
class ShoppingListRepository @Inject constructor(
    private val shoppingListDatabase: ShoppingListDatabaseDao,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun updateShoppingList(shoppingList: ShoppingList) {
        withContext(ioDispatcher) {
            shoppingListDatabase.updateShoppingList(shoppingList)
        }
    }

    suspend fun updateShoppingListItem(product: Product) {
        withContext(ioDispatcher) {
            shoppingListDatabase.updateShoppingListItem(product)
        }
    }

    suspend fun insertShoppingList(shoppingList: ShoppingList) {
        withContext(ioDispatcher) {
            shoppingListDatabase.insertShoppingList(shoppingList)
        }
    }

    suspend fun insertProduct(product: Product) {
        withContext(ioDispatcher) {
            shoppingListDatabase.insertShoppingListItem(product)
        }
    }

    suspend fun deleteProduct(product: Product) {
        withContext(ioDispatcher) {
            shoppingListDatabase.deleteShoppingListItem(product)
        }
    }

    fun getCurrentShoppingList() = shoppingListDatabase.getCurrentShoppingList()

    fun getArchivedShoppingList() = shoppingListDatabase.getArchivedShoppingList()

    fun getAllShoppingListItem(listId: Long) = shoppingListDatabase.getAllShoppingListItem(listId)
}