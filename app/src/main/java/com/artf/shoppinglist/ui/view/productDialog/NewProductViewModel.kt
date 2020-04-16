package com.artf.shoppinglist.ui.view.productDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artf.shoppinglist.data.database.model.Product
import com.artf.shoppinglist.data.repository.ShoppingListRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewProductViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val _createProductLoading = MutableLiveData<Boolean>()
    val createProductLoading: LiveData<Boolean> = _createProductLoading

    fun createProduct(name: String, quantity: Long, shoppingListId: Long) {
        _createProductLoading.value = true

        val product = Product(
            productName = name,
            productQuantity = quantity,
            shoppingListId = shoppingListId
        )
        viewModelScope.launch {
            shoppingListRepository.insertProduct(product)
            _createProductLoading.value = false
        }
    }
}