package com.artf.shoppinglist.ui.detailList

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.artf.shoppinglist.R
import com.artf.shoppinglist.databinding.FragmentProductListBinding
import com.artf.shoppinglist.model.ProductUi
import com.artf.shoppinglist.ui.MainActivity
import com.artf.shoppinglist.ui.SharedListViewModel
import com.artf.shoppinglist.ui.productDialog.NewProductDialog
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ProductListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sharedListViewModel: SharedListViewModel by activityViewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProductListBinding.inflate(LayoutInflater.from(context))
        binding.sharedListViewModel = sharedListViewModel
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = ProductListAdapter(getListItemListener())

        sharedListViewModel.selectedShoppingList.observe(this, Observer {
            NewProductDialog.shoppingListId = it.id
        })

        sharedListViewModel.createItem.observe(this, Observer {
            it?.let {
                val reviewDialog = NewProductDialog()
                reviewDialog.show(parentFragmentManager, NewProductDialog::class.simpleName)
                sharedListViewModel.onFabClicked(null)
            }
        })

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.setGroupVisible(R.id.menuGroup, false)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getListItemListener(): ProductListAdapter.ClickListenerInt {
        return object : ProductListAdapter.ClickListenerInt {
            override fun onClickListenerButton(product: ProductUi) {
                sharedListViewModel.deleteProduct(product)
            }

            override fun onClickListenerRow(product: ProductUi) {}
        }
    }

}