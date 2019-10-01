package com.artf.shoppinglist.ui.currentList

import androidx.lifecycle.*
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.repository.ShoppingListRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrentListViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    val shoppingLists = shoppingListRepository.getCurrentShoppingList()
    val isProductListEmpty = Transformations.map(shoppingLists) { it?.isEmpty() }

    private val _createShoppingList = MutableLiveData<Boolean>()
    val createShoppingList: LiveData<Boolean> = _createShoppingList
    fun onFabClicked(show: Boolean?) {
        _createShoppingList.value = show
    }
    private val _showSnackBar = MutableLiveData<Boolean>()
    val showSnackBar: LiveData<Boolean> = _showSnackBar
    fun setShowSnackBar (show: Boolean?){
        _showSnackBar.value = show
    }

    fun createShoppingList(name: String){
        val shoppingList = ShoppingList (shoppingListName = name)
        viewModelScope.launch {
            shoppingListRepository.insertShoppingList(shoppingList)
        }
    }

}