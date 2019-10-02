package com.artf.shoppinglist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.artf.shoppinglist.database.Product
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.repository.ShoppingListRepository
import com.artf.shoppinglist.util.ShoppingListType
import com.artf.shoppinglist.util.mock
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock

@RunWith(JUnit4::class)
class SharedViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val shoppingListRepository = mock(ShoppingListRepository::class.java)
    private val sharedViewModel = SharedViewModel(this.shoppingListRepository)
    private val shoppingList = mock(ShoppingList::class.java)

    @Test
    fun testNull() {
        MatcherAssert.assertThat(sharedViewModel.shoppingListType, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(sharedViewModel.selectedShoppingList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(sharedViewModel.shoppingLists, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(sharedViewModel.productList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(sharedViewModel.productListUi, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(sharedViewModel.isShoppingListsEmpty, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(sharedViewModel.isProductListsEmpty, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(sharedViewModel.createItem, CoreMatchers.notNullValue())
        Mockito.verify(shoppingListRepository, Mockito.never()).getCurrentShoppingList()
        Mockito.verify(shoppingListRepository, Mockito.never()).getArchivedShoppingList()
        Mockito.verify(shoppingListRepository, Mockito.never())
            .getAllShoppingListItem(Mockito.anyLong())
    }

    @Test
    fun testCallRepo() {
        sharedViewModel.shoppingLists.observeForever(mock())
        sharedViewModel.setShoppingListType(ShoppingListType.CURRENT)
        Mockito.verify(shoppingListRepository).getCurrentShoppingList()
        sharedViewModel.setShoppingListType(ShoppingListType.ARCHIVED)
        Mockito.verify(shoppingListRepository).getArchivedShoppingList()

        sharedViewModel.productList.observeForever(mock())
        sharedViewModel.onShoppingListClick(shoppingList)
        Mockito.verify(shoppingListRepository).getAllShoppingListItem(shoppingList.id)
    }

    @Test
    fun sendResultToUI() {
        val shoppingLists = MutableLiveData<List<ShoppingList>>()
        val productList = MutableLiveData<List<Product>>()
        Mockito.`when`(shoppingListRepository.getCurrentShoppingList()).thenReturn(shoppingLists)
        Mockito.`when`(shoppingListRepository.getArchivedShoppingList()).thenReturn(shoppingLists)
        Mockito.`when`(shoppingListRepository.getAllShoppingListItem(shoppingList.id))
            .thenReturn(productList)

        val observer1 = mock<Observer<ShoppingList>>()
        sharedViewModel.selectedShoppingList.observeForever(observer1)
        sharedViewModel.onShoppingListClick(shoppingList)
        Mockito.verify(observer1).onChanged(Mockito.any())

        val observer2 = mock<Observer<ShoppingListType>>()
        sharedViewModel.shoppingListType.observeForever(observer2)
        sharedViewModel.setShoppingListType(ShoppingListType.CURRENT)
        Mockito.verify(observer2).onChanged(Mockito.any())

        val observer3 = mock<Observer<Boolean>>()
        sharedViewModel.createItem.observeForever(observer3)
        sharedViewModel.onFabClicked(true)
        Mockito.verify(observer3).onChanged(Mockito.any())
    }

    @Test
    fun nullFab() {
        val observer = mock<Observer<Boolean>>()
        sharedViewModel.onFabClicked(true)
        sharedViewModel.onFabClicked(null)
        sharedViewModel.createItem.observeForever(observer)
        Mockito.verify(observer).onChanged(null)
    }

    @Test
    fun nullSelectedList() {
        val observer = mock<Observer<ShoppingList>>()
        sharedViewModel.onShoppingListClick(shoppingList)
        sharedViewModel.onShoppingListClick(null)
        sharedViewModel.selectedShoppingList.observeForever(observer)
        Mockito.verify(observer).onChanged(null)
    }

    @Test
    fun dontRefreshOnSameData() {
        val observer = mock<Observer<ShoppingListType>>()
        sharedViewModel.shoppingListType.observeForever(observer)
        Mockito.verifyNoMoreInteractions(observer)
        sharedViewModel.setShoppingListType(ShoppingListType.CURRENT)
        Mockito.verify(observer).onChanged(ShoppingListType.CURRENT)
        Mockito.reset(observer)
        sharedViewModel.setShoppingListType(ShoppingListType.CURRENT)
        Mockito.verifyNoMoreInteractions(observer)
        sharedViewModel.setShoppingListType(ShoppingListType.ARCHIVED)
        Mockito.verify(observer).onChanged(ShoppingListType.ARCHIVED)
    }
}