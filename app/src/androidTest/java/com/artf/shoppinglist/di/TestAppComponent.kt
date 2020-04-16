package com.artf.shoppinglist.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.artf.shoppinglist.TestApp
import com.artf.shoppinglist.data.repository.ShoppingListRepository
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        TestAppModule::class,
        TestMainActivityModule::class
    ]
)
interface TestAppComponent : AndroidInjector<TestApp> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): TestAppComponent
    }

    val shoppingListRepository: ShoppingListRepository

    val viewModelFactory: ViewModelProvider.Factory
}