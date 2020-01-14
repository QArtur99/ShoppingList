package com.artf.shoppinglist.ui.detailList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.artf.shoppinglist.R
import com.artf.shoppinglist.databinding.FragmentProductListBinding
import com.artf.shoppinglist.model.ProductUi
import com.artf.shoppinglist.ui.MainActivity
import com.artf.shoppinglist.ui.SharedViewModel
import com.artf.shoppinglist.ui.productDialog.NewProductDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductListFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProductListBinding.inflate(LayoutInflater.from(context))
        binding.sharedViewModel = sharedViewModel
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = ProductListAdapter(getListItemListener())

        sharedViewModel.selectedShoppingList.observe(viewLifecycleOwner, Observer {
            it?.let { NewProductDialog.shoppingListId = it.id }
        })

        sharedViewModel.createItem.observe(viewLifecycleOwner, Observer {
            it?.let {
                val reviewDialog = NewProductDialog()
                reviewDialog.show(parentFragmentManager, NewProductDialog::class.simpleName)
                sharedViewModel.onFabClicked(null)
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
                sharedViewModel.deleteProduct(product)
            }

            override fun onClickListenerRow(product: ProductUi) {}
        }
    }
}