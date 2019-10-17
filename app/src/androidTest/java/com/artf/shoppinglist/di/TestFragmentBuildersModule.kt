package com.artf.shoppinglist.di

import com.artf.shoppinglist.ui.CurrentListFragmentTest
import com.artf.shoppinglist.ui.archivedList.ArchivedListFragment
import com.artf.shoppinglist.ui.currentList.CurrentListFragment
import com.artf.shoppinglist.ui.detailList.ProductListFragment
import com.artf.shoppinglist.ui.productDialog.NewProductDialog
import com.artf.shoppinglist.ui.shoppingListDialog.NewListDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class TestFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeNewProductDialog(): NewProductDialog

    @ContributesAndroidInjector
    abstract fun contributeNewListDialog(): NewListDialog

    @ContributesAndroidInjector
    abstract fun contributeProductListFragment(): ProductListFragment

    @ContributesAndroidInjector
    abstract fun contributeArchivedListFragment(): ArchivedListFragment

    @ContributesAndroidInjector
    abstract fun contributeCurrentListFragment(): CurrentListFragment

    @ContributesAndroidInjector
    abstract fun contributeCurrentListFragmentTest(): CurrentListFragmentTest.TestCurrentListFragment
}
