package com.artf.shoppinglist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.artf.shoppinglist.MainCoroutineRule
import com.artf.shoppinglist.repository.ShoppingListRepository
import com.artf.shoppinglist.ui.productDialog.NewProductViewModel
import com.artf.shoppinglist.util.LiveDataTestUtil
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class NewProductViewModelTest {

    @Rule
    @JvmField
    @ExperimentalCoroutinesApi
    val mainCoroutineRule = MainCoroutineRule()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val shoppingListRepository = Mockito.mock(ShoppingListRepository::class.java)
    private val newProductVM = NewProductViewModel(shoppingListRepository)

    @Test
    fun createProduct() {
        mainCoroutineRule.pauseDispatcher()
        newProductVM.createProduct("Name", 5, 1L)
        Truth.assertThat(LiveDataTestUtil.getValue(newProductVM.createProductLoading)).isTrue()
        mainCoroutineRule.resumeDispatcher()
        Truth.assertThat(LiveDataTestUtil.getValue(newProductVM.createProductLoading)).isFalse()
    }
}