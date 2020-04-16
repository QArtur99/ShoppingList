package com.artf.shoppinglist.data.repository

import com.artf.shoppinglist.data.database.model.Product
import com.artf.shoppinglist.data.database.model.ShoppingList
import com.artf.shoppinglist.data.database.ShoppingListDatabaseDao
import com.artf.shoppinglist.testing.OpenForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@OpenForTesting
@Singleton
class ShoppingListRepositoryImpl @Inject constructor(
    private val shoppingListDatabase: ShoppingListDatabaseDao,
    private val ioDispatcher: CoroutineDispatcher
) : ShoppingListRepository {

    override suspend fun updateShoppingList(shoppingList: ShoppingList) {
        withContext(ioDispatcher) {
            shoppingListDatabase.updateShoppingList(shoppingList)
        }
    }

    override suspend fun updateShoppingListItem(product: Product) {
        withContext(ioDispatcher) {
            shoppingListDatabase.updateShoppingListItem(product)
        }
    }

    override suspend fun insertShoppingList(shoppingList: ShoppingList) {
        withContext(ioDispatcher) {
            shoppingListDatabase.insertShoppingList(shoppingList)
        }
    }

    override suspend fun insertProduct(product: Product) {
        withContext(ioDispatcher) {
            shoppingListDatabase.insertShoppingListItem(product)
        }
    }

    override suspend fun deleteProduct(product: Product) {
        withContext(ioDispatcher) {
            shoppingListDatabase.deleteShoppingListItem(product)
        }
    }

    override fun getCurrentShoppingList() = shoppingListDatabase.getCurrentShoppingList()

    override fun getArchivedShoppingList() = shoppingListDatabase.getArchivedShoppingList()

    override fun getAllShoppingListItem(listId: Long) = shoppingListDatabase.getAllShoppingListItem(listId)
}