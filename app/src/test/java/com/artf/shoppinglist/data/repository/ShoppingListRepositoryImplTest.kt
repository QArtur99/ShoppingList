package com.artf.shoppinglist.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.artf.shoppinglist.MainCoroutineRule
import com.artf.shoppinglist.data.database.model.Product
import com.artf.shoppinglist.data.database.model.ShoppingList
import com.artf.shoppinglist.data.database.ShoppingListDatabaseDao
import com.artf.shoppinglist.util.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class ShoppingListRepositoryImplTest {

    private val shoppingListDao = Mockito.mock(ShoppingListDatabaseDao::class.java)
    private val repo = ShoppingListRepositoryImpl(shoppingListDao, Dispatchers.Unconfined)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    @ExperimentalCoroutinesApi
    val mainCoroutineRule = MainCoroutineRule()

    @Test
    fun updateShoppingList() = runBlockingTest {
        val shoppingList =
            ShoppingList()
        repo.updateShoppingList(shoppingList)
        Mockito.verify(shoppingListDao).updateShoppingList(shoppingList)
    }

    @Test
    fun updateShoppingListItem() = runBlockingTest {
        val product = Product()
        repo.updateShoppingListItem(product)
        Mockito.verify(shoppingListDao).updateShoppingListItem(product)
    }

    @Test
    fun insertShoppingList() = runBlockingTest {
        val shoppingList =
            ShoppingList()
        repo.insertShoppingList(shoppingList)
        Mockito.verify(shoppingListDao).insertShoppingList(shoppingList)
    }

    @Test
    fun insertProduct() = runBlockingTest {
        val product = Product()
        repo.insertProduct(product)
        Mockito.verify(shoppingListDao).insertShoppingListItem(product)
    }

    @Test
    fun deleteProduct() = runBlockingTest {
        val product = Product()
        repo.deleteProduct(product)
        Mockito.verify(shoppingListDao).deleteShoppingListItem(product)
    }

    @Test
    fun getCurrentShoppingList() {
        val shoppingLists = emptyList<ShoppingList>()
        val shoppingListsLiveData = MutableLiveData<List<ShoppingList>>().apply { value = shoppingLists }
        Mockito.`when`(shoppingListDao.getCurrentShoppingList()).thenReturn(shoppingListsLiveData)
        val observer = mock<Observer<List<ShoppingList>>>()
        repo.getCurrentShoppingList().observeForever(observer)
        Mockito.verify(observer).onChanged(shoppingLists)
        Mockito.verify(shoppingListDao).getCurrentShoppingList()
    }

    @Test
    fun getArchivedShoppingList() {
        val shoppingLists = emptyList<ShoppingList>()
        val shoppingListsLiveData = MutableLiveData<List<ShoppingList>>().apply { value = shoppingLists }
        Mockito.`when`(shoppingListDao.getArchivedShoppingList()).thenReturn(shoppingListsLiveData)
        val observer = mock<Observer<List<ShoppingList>>>()
        repo.getArchivedShoppingList().observeForever(observer)
        Mockito.verify(observer).onChanged(shoppingLists)
        Mockito.verify(shoppingListDao).getArchivedShoppingList()
    }

    @Test
    fun getAllShoppingListItem() {
        val listId = 0L
        val product = emptyList<Product>()
        val productListLiveData = MutableLiveData<List<Product>>().apply { value = product }
        Mockito.`when`(shoppingListDao.getAllShoppingListItem(listId)).thenReturn(productListLiveData)
        val observer = mock<Observer<List<Product>>>()
        repo.getAllShoppingListItem(listId).observeForever(observer)
        Mockito.verify(observer).onChanged(product)
        Mockito.verify(shoppingListDao).getAllShoppingListItem(listId)
    }
}