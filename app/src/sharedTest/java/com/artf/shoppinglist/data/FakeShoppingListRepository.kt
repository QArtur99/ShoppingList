package com.artf.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artf.shoppinglist.database.Product
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.repository.ShoppingListRepositoryInt
import com.artf.shoppinglist.testing.OpenForTesting

@OpenForTesting
class FakeShoppingListRepository : ShoppingListRepositoryInt {

    var shoppingListData: MutableList<ShoppingList> = mutableListOf()
    var productData: MutableList<Product> = mutableListOf()

    var shouldReturnError = false

    override suspend fun updateShoppingList(shoppingList: ShoppingList) {
        shoppingListData.find { it == shoppingList }?.apply {
            shoppingListName = shoppingList.shoppingListName
            isArchived = shoppingList.isArchived
        }
    }

    override suspend fun updateShoppingListItem(product: Product) {
        productData.find { it == product }?.apply {
            productName = product.productName
            productQuantity = product.productQuantity
            shoppingListId = product.shoppingListId

        }
    }

    override suspend fun insertShoppingList(shoppingList: ShoppingList) {
        shoppingListData.add(shoppingList)
    }

    override suspend fun insertProduct(product: Product) {
        productData.add(product)
    }

    override suspend fun deleteProduct(product: Product) {
        productData.remove(product)
    }

    override fun getCurrentShoppingList(): LiveData<List<ShoppingList>> {
        return MutableLiveData<List<ShoppingList>>().apply {
            value = shoppingListData.takeWhile { it.isArchived.not() }
        }
    }

    override fun getArchivedShoppingList(): LiveData<List<ShoppingList>> {
        return MutableLiveData<List<ShoppingList>>().apply {
            value = shoppingListData.takeWhile { it.isArchived }
        }
    }

    override fun getAllShoppingListItem(listId: Long): LiveData<List<Product>> {
        return MutableLiveData<List<Product>>().apply {
            value = productData.takeWhile { it.shoppingListId == listId }
        }
    }
}