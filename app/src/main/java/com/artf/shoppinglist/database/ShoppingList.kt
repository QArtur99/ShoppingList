package com.artf.shoppinglist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.artf.shoppinglist.testing.OpenForTesting

@OpenForTesting
@Entity(tableName = "shopping_list")
data class ShoppingList(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "shoppingListName")
    var shoppingListName: String = "",

    @ColumnInfo(name = "shoppingListTimestamp")
    var shoppingListTimestamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "isArchived")
    var isArchived: Boolean = false
)