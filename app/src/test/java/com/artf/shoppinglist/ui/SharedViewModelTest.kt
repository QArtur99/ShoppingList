package com.artf.shoppinglist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.artf.shoppinglist.MainCoroutineRule
import com.artf.shoppinglist.data.FakeShoppingListRepository
import com.artf.shoppinglist.database.Product
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.repository.ShoppingListRepository
import com.artf.shoppinglist.util.LiveDataTestUtil.getValue
import com.artf.shoppinglist.util.ShoppingListType
import com.artf.shoppinglist.util.asUiModel
import com.artf.shoppinglist.util.mock
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
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
    private val shoppingList = mock(ShoppingList::class.java)

    private lateinit var fakeShoppingListRepository: FakeShoppingListRepository
    //private lateinit var sharedViewModel : SharedViewModel

    // @Before
    // fun setupViewModel() {
    //     fakeShoppingListRepository.shoppingListData.apply {
    //         add(ShoppingList(0L, "0"))
    //         add(ShoppingList(1L, "1"))
    //         add(ShoppingList(2L, "2"))
    //     }
    //
    //     fakeShoppingListRepository.productData.apply {
    //         add(Product(0L, "0", 1, shoppingListId= 0L))
    //         add(Product(1L, "1", 1, shoppingListId= 1L))
    //         add(Product(2L, "2", 1, shoppingListId= 2L))
    //     }
    //
    //     sharedViewModel = SharedViewModel(fakeShoppingListRepository)
    // }

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
        MatcherAssert.assertThat(
            sharedViewModel.updateShoppingListLoading, CoreMatchers.notNullValue()
        )
        MatcherAssert.assertThat(sharedViewModel.deleteProductLoading, CoreMatchers.notNullValue())
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

    @Test
    fun updateShoppingListLoading() {
        mainCoroutineRule.pauseDispatcher()
        sharedViewModel.updateShoppingList(shoppingList, false)
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