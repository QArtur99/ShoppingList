package com.artf.shoppinglist.ui.shoppingListDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.repository.ShoppingListRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewListViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    fun createShoppingList(name: String) {
        val shoppingList = ShoppingList(shoppingListName = name)
        viewModelScope.launch {
            shoppingListRepository.insertShoppingList(shoppingList)
        }
    }
}