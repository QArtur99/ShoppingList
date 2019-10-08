package com.artf.shoppinglist.di

import android.content.Context
import com.artf.shoppinglist.database.ShoppingListDatabase
import com.artf.shoppinglist.database.ShoppingListDatabaseDao
import com.artf.shoppinglist.di.viewModel.ViewModelModule
import com.artf.shoppinglist.repository.ShoppingListRepository
import com.artf.shoppinglist.repository.ShoppingListRepositoryInt
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
object AppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideDataBase(context: Context): ShoppingListDatabaseDao {
        return ShoppingListDatabase.getInstance(context).shoppingListDatabaseDao
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRepository(
        shoppingListDatabase: ShoppingListDatabaseDao,
        ioDispatcher: CoroutineDispatcher
    ): ShoppingListRepositoryInt {
        return ShoppingListRepository(shoppingListDatabase, ioDispatcher)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.Default
}
