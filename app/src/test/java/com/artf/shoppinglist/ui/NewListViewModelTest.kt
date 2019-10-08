package com.artf.shoppinglist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.artf.shoppinglist.MainCoroutineRule
import com.artf.shoppinglist.repository.ShoppingListRepository
import com.artf.shoppinglist.ui.shoppingListDialog.NewListViewModel
import com.artf.shoppinglist.util.LiveDataTestUtil
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class NewListViewModelTest {

    @Rule
    @JvmField
    @ExperimentalCoroutinesApi
    val mainCoroutineRule = MainCoroutineRule()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val shoppingListRepository = Mockito.mock(ShoppingListRepository::class.java)
    private val newListVM = NewListViewModel(shoppingListRepository)

    @Test
    fun createProduct() {
        mainCoroutineRule.pauseDispatcher()
        newListVM.createShoppingList("Name")
        Truth.assertThat(LiveDataTestUtil.getValue(newListVM.createShoppingListLoading)).isTrue()
        mainCoroutineRule.resumeDispatcher()
        Truth.assertThat(LiveDataTestUtil.getValue(newListVM.createShoppingListLoading)).isFalse()
    }
}