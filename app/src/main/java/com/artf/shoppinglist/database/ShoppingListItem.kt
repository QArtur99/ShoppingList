package com.artf.shoppinglist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list_item")
data class ShoppingListItem(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "productName")
    var productName: String = "",

    @ColumnInfo(name = "currentQuantity")
    var productQuantity: Long = -1L,

    @ColumnInfo(name = "productTimestamp")
    var productTimestamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "productImage")
    var shoppingListId: Long = 0L
)