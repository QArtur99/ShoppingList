package com.artf.shoppinglist.di

import com.artf.shoppinglist.ui.currentList.CurrentListFragment
import com.artf.shoppinglist.ui.NewListDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
//    @ContributesAndroidInjector
//    abstract fun contributeArchivedListFragment(): ArchivedListFragment

    @ContributesAndroidInjector
    abstract fun contributeNewListDialog(): NewListDialog

    @ContributesAndroidInjector
    abstract fun contributeCurrentListFragment(): CurrentListFragment
}
