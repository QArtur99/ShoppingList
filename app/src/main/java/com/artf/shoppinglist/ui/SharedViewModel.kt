package com.artf.shoppinglist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artf.shoppinglist.database.Product
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.model.ProductUi
import com.artf.shoppinglist.repository.ShoppingListRepository
import com.artf.shoppinglist.util.ShoppingListType
import com.artf.shoppinglist.util.asDbModel
import com.artf.shoppinglist.util.asUiModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SharedViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val _shoppingListType = MutableLiveData<ShoppingListType>()
    val shoppingListType: LiveData<ShoppingListType> = _shoppingListType
    fun setShoppingListType(shoppingListType: ShoppingListType) {
        if (_shoppingListType.value != shoppingListType)
            _shoppingListType.value = shoppingListType
    }

    private val _selectedShoppingList = MutableLiveData<ShoppingList>()
    val selectedShoppingList: LiveData<ShoppingList> = _selectedShoppingList
    fun onShoppingListClick(shoppingList: ShoppingList?) {
        _selectedShoppingList.value = shoppingList
    }

    val shoppingLists = Transformations.switchMap(shoppingListType) {
        when (it) {
            ShoppingListType.CURRENT -> shoppingListRepository.getCurrentShoppingList()
            ShoppingListType.ARCHIVED -> shoppingListRepository.getArchivedShoppingList()
            else -> throw IllegalArgumentException("unknown ShoppingListType $it")
        }
    }

    val productList = Transformations.switchMap(_selectedShoppingList) {
        if (it == null) {
            MutableLiveData<List<Product>>().apply { value = null }
        } else {
            shoppingListRepository.getAllShoppingListItem(it.id)
        }
    }

    val productListUi = Transformations.map(productList) { it?.asUiModel(shoppingListType.value!!) }
    val isShoppingListsEmpty = Transformations.map(shoppingLists) { it?.isEmpty() ?: false }
    val isProductListsEmpty = Transformations.map(productListUi) { it?.isEmpty() ?: false }

    private val _createItem = MutableLiveData<Boolean>()
    val createItem: LiveData<Boolean> = _createItem
    fun onFabClicked(show: Boolean?) {
        _createItem.value = show
    }

    fun updateShoppingList(shoppingList: ShoppingList, isArchived: Boolean) {
        shoppingList.isArchived = isArchived
        viewModelScope.launch {
            shoppingListRepository.updateShoppingList(shoppingList)
        }
    }

    fun deleteProduct(product: ProductUi) {
        viewModelScope.launch {
            shoppingListRepository.deleteProduct(product.asDbModel())
        }
    }
}
