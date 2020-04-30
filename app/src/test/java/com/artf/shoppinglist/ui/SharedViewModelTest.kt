package com.artf.shoppinglist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.artf.shoppinglist.MainCoroutineRule
import com.artf.shoppinglist.data.database.model.Product
import com.artf.shoppinglist.data.database.model.ShoppingList
import com.artf.shoppinglist.data.repository.ShoppingListRepository
import com.artf.shoppinglist.util.LiveDataTestUtil.getValue
import com.artf.shoppinglist.util.ShoppingListType
import com.artf.shoppinglist.ui.data.mapper.asUiModel
import com.artf.shoppinglist.ui.data.model.ShoppingListUi
import com.artf.shoppinglist.ui.view.SharedViewModel
import com.artf.shoppinglist.util.mock
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.never
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class SharedViewModelTest {

    @Rule
    @JvmField
    @ExperimentalCoroutinesApi
    val mainCoroutineRule = MainCoroutineRule()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val shoppingListRepository = mock(ShoppingListRepository::class.java)
    private val sharedViewModel = SharedViewModel(shoppingListRepository)
    private val shoppingList = mock(ShoppingListUi::class.java)

    @Test
    fun testNull() {
        assertThat(sharedViewModel.shoppingListType, CoreMatchers.notNullValue())
        assertThat(sharedViewModel.selectedShoppingList, CoreMatchers.notNullValue())
        assertThat(sharedViewModel.shoppingLists, CoreMatchers.notNullValue())
        assertThat(sharedViewModel.productList, CoreMatchers.notNullValue())
        assertThat(sharedViewModel.productListUi, CoreMatchers.notNullValue())
        assertThat(sharedViewModel.isShoppingListsEmpty, CoreMatchers.notNullValue())
        assertThat(sharedViewModel.isProductListsEmpty, CoreMatchers.notNullValue())
        assertThat(sharedViewModel.createItem, CoreMatchers.notNullValue())
        assertThat(sharedViewModel.updateShoppingListLoading, CoreMatchers.notNullValue())
        assertThat(sharedViewModel.deleteProductLoading, CoreMatchers.notNullValue())
        verify(shoppingListRepository, never()).getCurrentShoppingList()
        verify(shoppingListRepository, never()).getArchivedShoppingList()
        verify(shoppingListRepository, never()).getAllShoppingListItem(Mockito.anyLong())
    }

    @Test
    fun testCallRepo() {
        sharedViewModel.shoppingLists.observeForever(mock())
        sharedViewModel.setShoppingListType(ShoppingListType.CURRENT)
        verify(shoppingListRepository).getCurrentShoppingList()
        sharedViewModel.setShoppingListType(ShoppingListType.ARCHIVED)
        verify(shoppingListRepository).getArchivedShoppingList()
        assertThat(getValue(sharedViewModel.shoppingLists)).isNull()
        assertThat(getValue(sharedViewModel.isShoppingListsEmpty)).isNull()

        val shoppingLists = MutableLiveData<List<ShoppingList>>().apply { value = emptyList() }
        Mockito.`when`(shoppingListRepository.getCurrentShoppingList()).thenReturn(shoppingLists)
        sharedViewModel.isShoppingListsEmpty.observeForever(mock())
        sharedViewModel.setShoppingListType(ShoppingListType.CURRENT)
        assertThat(getValue(sharedViewModel.shoppingLists)).isEmpty()
        assertThat(getValue(sharedViewModel.isShoppingListsEmpty)).isTrue()
    }

    @Test
    fun cleanProductList() {
        sharedViewModel.productList.observeForever(mock())
        sharedViewModel.onShoppingListClick(null)
        assertThat(getValue(sharedViewModel.productList)).isNull()
        assertThat(getValue(sharedViewModel.productListUi)).isNull()
        assertThat(getValue(sharedViewModel.isProductListsEmpty)).isFalse()
        sharedViewModel.onShoppingListClick(shoppingList)
        verify(shoppingListRepository).getAllShoppingListItem(shoppingList.id)
    }

    @Test
    fun sendResultToUI() {
        val shoppingLists = MutableLiveData<List<ShoppingList>>()
        val productList = MutableLiveData<List<Product>>()
        Mockito.`when`(shoppingListRepository.getCurrentShoppingList()).thenReturn(shoppingLists)
        Mockito.`when`(shoppingListRepository.getArchivedShoppingList()).thenReturn(shoppingLists)
        Mockito.`when`(shoppingListRepository.getAllShoppingListItem(shoppingList.id))
            .thenReturn(productList)

        val observer1 = mock<Observer<ShoppingListUi>>()
        sharedViewModel.selectedShoppingList.observeForever(observer1)
        sharedViewModel.onShoppingListClick(shoppingList)
        verify(observer1).onChanged(Mockito.any())

        val observer2 = mock<Observer<ShoppingListType>>()
        sharedViewModel.shoppingListType.observeForever(observer2)
        sharedViewModel.setShoppingListType(ShoppingListType.CURRENT)
        verify(observer2).onChanged(Mockito.any())

        val observer3 = mock<Observer<Boolean>>()
        sharedViewModel.createItem.observeForever(observer3)
        sharedViewModel.onFabClicked(true)
        verify(observer3).onChanged(Mockito.any())
    }

    @Test
    fun nullFab() {
        val observer = mock<Observer<Boolean>>()
        sharedViewModel.createItem.observeForever(observer)
        sharedViewModel.onFabClicked(true)
        sharedViewModel.onFabClicked(null)
        verify(observer).onChanged(null)
    }

    @Test
    fun nullSelectedList() {
        val observer = mock<Observer<ShoppingListUi>>()
        sharedViewModel.onShoppingListClick(shoppingList)
        sharedViewModel.onShoppingListClick(null)
        sharedViewModel.selectedShoppingList.observeForever(observer)
        verify(observer).onChanged(null)
    }

    @Test
    fun dontRefreshOnSameData() {
        val observer = mock<Observer<ShoppingListType>>()
        sharedViewModel.shoppingListType.observeForever(observer)
        Mockito.verifyNoMoreInteractions(observer)
        sharedViewModel.setShoppingListType(ShoppingListType.CURRENT)
        verify(observer).onChanged(ShoppingListType.CURRENT)
        Mockito.reset(observer)
        sharedViewModel.setShoppingListType(ShoppingListType.CURRENT)
        Mockito.verifyNoMoreInteractions(observer)
        sharedViewModel.setShoppingListType(ShoppingListType.ARCHIVED)
        verify(observer).onChanged(ShoppingListType.ARCHIVED)
    }

    @Test
    fun updateShoppingListLoading() {
        val shoppingListUi = ShoppingListUi(0, "name", 0L, true)
        mainCoroutineRule.pauseDispatcher()
        sharedViewModel.updateShoppingList(shoppingListUi, false)
        assertThat(getValue(sharedViewModel.updateShoppingListLoading)).isTrue()
        mainCoroutineRule.resumeDispatcher()
        assertThat(getValue(sharedViewModel.updateShoppingListLoading)).isFalse()
    }

    @Test
    fun deleteProductLoading() {
        val product = Product(1L, "Name")
        val productUi = product.asUiModel(ShoppingListType.CURRENT)
        mainCoroutineRule.pauseDispatcher()
        sharedViewModel.deleteProduct(productUi)
        assertThat(getValue(sharedViewModel.deleteProductLoading)).isTrue()
        mainCoroutineRule.resumeDispatcher()
        assertThat(getValue(sharedViewModel.deleteProductLoading)).isFalse()
    }
}