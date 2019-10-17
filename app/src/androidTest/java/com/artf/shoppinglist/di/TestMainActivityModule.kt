package com.artf.shoppinglist.di

import com.artf.shoppinglist.testing.SingleFragmentActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class TestMainActivityModule {
    @ContributesAndroidInjector(modules = [TestFragmentBuildersModule::class])
    abstract fun contributeMainActivity(): SingleFragmentActivity
}
