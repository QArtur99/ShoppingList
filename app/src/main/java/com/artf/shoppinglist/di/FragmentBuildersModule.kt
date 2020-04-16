package com.artf.shoppinglist.di

import com.artf.shoppinglist.ui.view.archivedList.ArchivedListFragment
import com.artf.shoppinglist.ui.view.currentList.CurrentListFragment
import com.artf.shoppinglist.ui.view.detailList.ProductListFragment
import com.artf.shoppinglist.ui.view.productDialog.NewProductDialog
import com.artf.shoppinglist.ui.view.shoppingListDialog.NewListDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

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
}
