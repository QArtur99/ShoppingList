package com.artf.shoppinglist.ui.view.currentList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artf.shoppinglist.R
import com.artf.shoppinglist.databinding.FragmentCurrentListBinding
import com.artf.shoppinglist.ui.data.model.ShoppingListUi
import com.artf.shoppinglist.ui.view.SharedViewModel
import com.artf.shoppinglist.ui.view.shoppingListDialog.NewListDialog
import com.artf.shoppinglist.util.ShoppingListType
import dagger.android.support.DaggerFragment
import javax.inject.Inject

open class CurrentListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sharedViewModel: SharedViewModel by activityViewModels { viewModelFactory() }

    lateinit var binding: FragmentCurrentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentListBinding.inflate(LayoutInflater.from(context))
        binding.sharedViewModel = sharedViewModel
        binding.lifecycleOwner = this
        binding.recyclerView.adapter =
            CurrentListAdapter(
                getListItemListener()
            )

        sharedViewModel.setShoppingListType(ShoppingListType.CURRENT)
        sharedViewModel.createItem.observe(this, Observer {
            it?.let {
                val reviewDialog =
                    NewListDialog()
                reviewDialog.show(parentFragmentManager, NewListDialog::class.simpleName)
                sharedViewModel.onFabClicked(null)
            }
        })

        sharedViewModel.shoppingListType.observe(this, Observer {
            if (isThisDestination().not()) return@Observer
            if (it != ShoppingListType.ARCHIVED) return@Observer
            navController().navigate(R.id.action_current_list_to_archived_list)
        })

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        return binding.root
    }

    private fun getListItemListener(): CurrentListAdapter.ClickListener {
        return object : CurrentListAdapter.ClickListener {
            override fun onClickListenerButton(shoppingList: ShoppingListUi) {
                sharedViewModel.updateShoppingList(shoppingList, true)
            }

            override fun onClickListenerRow(shoppingList: ShoppingListUi) {
                sharedViewModel.onShoppingListClick(shoppingList)
                if (isThisDestination().not()) return
                navController().navigate(R.id.action_current_list_to_product_list)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.onShoppingListClick(null)
    }

    /**
     * Created to be able to override in tests
     */

    open fun isThisDestination(): Boolean {
        return navController().currentDestination?.id == R.id.fragment_current_list
    }

    open fun navController() = findNavController()

    open fun viewModelFactory() = viewModelFactory
}