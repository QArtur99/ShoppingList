package com.artf.shoppinglist.ui.productDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artf.shoppinglist.database.Product
import com.artf.shoppinglist.repository.ShoppingListRepositoryInt
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewProductViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepositoryInt
) : ViewModel() {

    fun createProduct(name: String, quantity: Long, shoppingListId: Long) {
        val product = Product(
            productName = name,
            productQuantity = quantity,
            shoppingListId = shoppingListId
        )
        viewModelScope.launch {
            shoppingListRepository.insertProduct(product)
        }
    }
}