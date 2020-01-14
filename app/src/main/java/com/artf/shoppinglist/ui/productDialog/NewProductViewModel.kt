package com.artf.shoppinglist.ui.productDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artf.shoppinglist.database.Product
import com.artf.shoppinglist.repository.ShoppingListRepositoryInt
import kotlinx.coroutines.launch

class NewProductViewModel constructor(
    private val shoppingListRepository: ShoppingListRepositoryInt
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