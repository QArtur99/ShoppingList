package com.artf.shoppinglist.ui.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artf.shoppinglist.data.database.model.Product
import com.artf.shoppinglist.ui.data.model.ProductUi
import com.artf.shoppinglist.data.repository.ShoppingListRepository
import com.artf.shoppinglist.testing.OpenForTesting
import com.artf.shoppinglist.util.ShoppingListType
import com.artf.shoppinglist.ui.data.mapper.asDbModel
import com.artf.shoppinglist.ui.data.mapper.asDomainModel
import com.artf.shoppinglist.ui.data.mapper.asUiModel
import com.artf.shoppinglist.ui.data.model.ShoppingListUi
import kotlinx.coroutines.launch
import javax.inject.Inject

@OpenForTesting
class SharedViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val _shoppingListType = MutableLiveData<ShoppingListType>()
    val shoppingListType: LiveData<ShoppingListType> = _shoppingListType

    private val _selectedShoppingList = MutableLiveData<ShoppingListUi>()
    val selectedShoppingList: LiveData<ShoppingListUi> = _selectedShoppingList

    private val _createItem = MutableLiveData<Boolean>()
    val createItem: LiveData<Boolean> = _createItem

    private val _updateShoppingListLoading = MutableLiveData<Boolean>()
    val updateShoppingListLoading: LiveData<Boolean> = _updateShoppingListLoading

    private val _deleteProductLoading = MutableLiveData<Boolean>()
    val deleteProductLoading: LiveData<Boolean> = _deleteProductLoading

    val shoppingLists = Transformations.switchMap(shoppingListType) {
        when (it) {
            ShoppingListType.CURRENT -> shoppingListRepository.getCurrentShoppingList()
            ShoppingListType.ARCHIVED -> shoppingListRepository.getArchivedShoppingList()
        }
    }

    val productList = Transformations.switchMap(_selectedShoppingList) {
        if (it == null) {
            MutableLiveData<List<Product>>().apply { value = null }
        } else {
            shoppingListRepository.getAllShoppingListItem(it.id)
        }
    }

    val shoppingListsUi = Transformations.map(shoppingLists) { it?.asUiModel() }
    val isShoppingListsEmpty = Transformations.map(shoppingLists) { it?.isEmpty() ?: false }
    val productListUi = Transformations.map(productList) { it?.asUiModel(shoppingListType.value!!) }
    val isProductListsEmpty = Transformations.map(productListUi) { it?.isEmpty() ?: false }

    fun setShoppingListType(shoppingListType: ShoppingListType) {
        if (_shoppingListType.value != shoppingListType)
            _shoppingListType.value = shoppingListType
    }

    fun onShoppingListClick(shoppingList: ShoppingListUi?) {
        _selectedShoppingList.value = shoppingList
    }

    fun onFabClicked(show: Boolean?) {
        _createItem.value = show
    }

    fun updateShoppingList(shoppingList: ShoppingListUi, isArchived: Boolean) {
        _updateShoppingListLoading.value = true
        shoppingList.isArchived = isArchived
        viewModelScope.launch {
            shoppingListRepository.updateShoppingList(shoppingList.asDomainModel())
            _updateShoppingListLoading.value = false
        }
    }

    fun deleteProduct(product: ProductUi) {
        _deleteProductLoading.value = true
        viewModelScope.launch {
            shoppingListRepository.deleteProduct(product.asDbModel())
            _deleteProductLoading.value = false
        }
    }
}
