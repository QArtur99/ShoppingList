package com.artf.shoppinglist.di

import android.content.Context
import com.artf.shoppinglist.database.ShoppingListDatabase
import com.artf.shoppinglist.database.ShoppingListDatabaseDao
import com.artf.shoppinglist.repository.ShoppingListRepository
import com.artf.shoppinglist.repository.ShoppingListRepositoryInt
import com.artf.shoppinglist.ui.SharedViewModel
import com.artf.shoppinglist.ui.productDialog.NewProductViewModel
import com.artf.shoppinglist.ui.shoppingListDialog.NewListViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {

    viewModel { SharedViewModel(get()) }

    viewModel { NewProductViewModel(get()) }

    viewModel { NewListViewModel(get()) }
}

val dataModule = module {

    single { createRepository(get(), get()) }

    single { createDataBase(get()) }

    single { createIoDispatcher() }
}

fun createIoDispatcher() = Dispatchers.Default

fun createDataBase(context: Context): ShoppingListDatabaseDao {
    return ShoppingListDatabase.getInstance(context).shoppingListDatabaseDao
}

fun createRepository(
    shoppingListDatabase: ShoppingListDatabaseDao,
    ioDispatcher: CoroutineDispatcher
): ShoppingListRepositoryInt {
    return ShoppingListRepository(shoppingListDatabase, ioDispatcher)
}