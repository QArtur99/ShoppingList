package com.artf.shoppinglist.di

import android.content.Context
import com.artf.shoppinglist.database.ShoppingListDatabaseDao
import com.artf.shoppinglist.database.ShoppingListDatabaseJava
import com.artf.shoppinglist.di.viewModel.ViewModelModule
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
object AppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideDataBase(context: Context): ShoppingListDatabaseDao {
        return ShoppingListDatabaseJava.getInstance(context).shoppingListDatabaseDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.Default
}
