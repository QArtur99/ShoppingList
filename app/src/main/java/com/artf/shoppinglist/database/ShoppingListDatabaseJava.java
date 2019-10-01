package com.artf.shoppinglist.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ShoppingList.class, Product.class}, version = 2, exportSchema = false)
public abstract class ShoppingListDatabaseJava extends RoomDatabase {
    private static volatile ShoppingListDatabaseJava instance;

    public static synchronized ShoppingListDatabaseJava getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ShoppingListDatabaseJava.class,
                    "shopping_list_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract ShoppingListDatabaseDao shoppingListDatabaseDao();
}
