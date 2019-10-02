package com.artf.shoppinglist.di.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artf.shoppinglist.ui.SharedViewModel
import com.artf.shoppinglist.ui.productDialog.NewProductViewModel
import com.artf.shoppinglist.ui.shoppingListDialog.NewListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewProductViewModel::class)
    abstract fun bindNewProductViewModel(viewModel: NewProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewListViewModel::class)
    abstract fun bindNewListViewModel(viewModel: NewListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SharedViewModel::class)
    abstract fun bindSharedListViewModel(viewModel: SharedViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}