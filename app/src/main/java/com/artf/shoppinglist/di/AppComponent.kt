package com.artf.shoppinglist.di

import android.content.Context
import com.artf.shoppinglist.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        MainActivityModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }
}