package com.artf.shoppinglist.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingListDatabaseDao {

    @Insert
    fun insertShoppingList(shoppingList: ShoppingList)

    @Insert
    fun insertShoppingListItem(shoppingListItem: ShoppingListItem)

    @Update
    fun updateShoppingList(shoppingList: ShoppingList)

    @Update
    fun updateShoppingListItem(shoppingListItem: ShoppingListItem)

    @Delete
    fun deleteShoppingListItem(shoppingListItem: ShoppingListItem)

    @Query("SELECT * FROM shopping_list WHERE isArchived IS 0 ORDER BY shoppingListTimestamp DESC")
    fun getCurrentShoppingList(): LiveData<List<ShoppingList>>

    @Query("SELECT * FROM shopping_list WHERE isArchived IS 1 ORDER BY shoppingListTimestamp DESC")
    fun getArchivedShoppingList(): LiveData<List<ShoppingList>>

    @Query("SELECT * FROM shopping_list_item ORDER BY productTimestamp DESC")
    fun getAllShoppingListItem(): LiveData<List<ShoppingListItem>>

}

