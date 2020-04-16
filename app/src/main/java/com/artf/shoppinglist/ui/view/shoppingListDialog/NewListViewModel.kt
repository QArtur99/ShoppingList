package com.artf.shoppinglist.ui.view.shoppingListDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artf.shoppinglist.data.database.model.ShoppingList
import com.artf.shoppinglist.data.repository.ShoppingListRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewListViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val _createShoppingListLoading = MutableLiveData<Boolean>()
    val createShoppingListLoading: LiveData<Boolean> = _createShoppingListLoading

    fun createShoppingList(name: String) {
        _createShoppingListLoading.value = true
        val shoppingList =
            ShoppingList(
                shoppingListName = name
            )
        viewModelScope.launch {
            shoppingListRepository.insertShoppingList(shoppingList)
            _createShoppingListLoading.value = false
        }
    }
}