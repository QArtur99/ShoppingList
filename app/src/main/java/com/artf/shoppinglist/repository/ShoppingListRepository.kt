package com.artf.shoppinglist.repository

import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.database.ShoppingListDatabaseDao
import com.artf.shoppinglist.database.ShoppingListItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val shoppingListDatabase: ShoppingListDatabaseDao,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun updateShoppingList(shoppingList: ShoppingList) {
        withContext(ioDispatcher) {
            shoppingListDatabase.updateShoppingList(shoppingList)
        }
    }

    suspend fun updateShoppingListItem(shoppingListItem: ShoppingListItem) {
        withContext(ioDispatcher) {
            shoppingListDatabase.updateShoppingListItem(shoppingListItem)
        }
    }

    suspend fun insertShoppingList(shoppingList: ShoppingList) {
        withContext(ioDispatcher) {
            shoppingListDatabase.insertShoppingList(shoppingList)
        }
    }

    suspend fun insertShoppingListItem(shoppingListItem: ShoppingListItem) {
        withContext(ioDispatcher) {
            shoppingListDatabase.insertShoppingListItem(shoppingListItem)
        }
    }

    suspend fun deleteShoppingListItem(shoppingListItem: ShoppingListItem) {
        withContext(ioDispatcher) {
            shoppingListDatabase.deleteShoppingListItem(shoppingListItem)
        }
    }

    fun getCurrentShoppingList() = shoppingListDatabase.getCurrentShoppingList()

    fun getArchivedShoppingList() = shoppingListDatabase.getArchivedShoppingList()

    fun getAllShoppingListItem() = shoppingListDatabase.getAllShoppingListItem()

}