package com.artf.shoppinglist.ui.currentList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artf.shoppinglist.R
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.databinding.FragmentCurrentListBinding
import com.artf.shoppinglist.ui.MainActivity
import com.artf.shoppinglist.ui.SharedViewModel
import com.artf.shoppinglist.ui.shoppingListDialog.NewListDialog
import com.artf.shoppinglist.util.ShoppingListType
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CurrentListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sharedViewModel: SharedViewModel by activityViewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCurrentListBinding.inflate(LayoutInflater.from(context))
        binding.sharedViewModel = sharedViewModel
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = CurrentListAdapter(getListItemListener())

        sharedViewModel.setShoppingListType(ShoppingListType.CURRENT)
        sharedViewModel.createItem.observe(this, Observer {
            it?.let {
                val reviewDialog = NewListDialog()
                reviewDialog.show(parentFragmentManager, NewListDialog::class.simpleName)
                sharedViewModel.onFabClicked(null)
            }
        })

        sharedViewModel.shoppingListType.observe(this, Observer {
            if (isThisDestination().not()) return@Observer
            if (it != ShoppingListType.ARCHIVED) return@Observer
            findNavController().navigate(R.id.action_current_list_to_archived_list)
        })

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        return binding.root
    }

    private fun getListItemListener(): CurrentListAdapter.ClickListenerInt {
        return object : CurrentListAdapter.ClickListenerInt {
            override fun onClickListenerButton(shoppingList: ShoppingList) {
                sharedViewModel.updateShoppingList(shoppingList, true)
            }

            override fun onClickListenerRow(shoppingList: ShoppingList) {
                sharedViewModel.onShoppingListClick(ShoppingList(-1L))
                sharedViewModel.onShoppingListClick(shoppingList)
                if (isThisDestination().not()) return
                findNavController().navigate(R.id.action_current_list_to_product_list)
            }
        }
    }

    fun isThisDestination(): Boolean {
        return findNavController().currentDestination?.id == R.id.fragment_current_list
    }

}