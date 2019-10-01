package com.artf.shoppinglist.ui.currentList

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artf.shoppinglist.R
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.databinding.FragmentCurrentListBinding
import com.artf.shoppinglist.ui.MainActivity
import com.artf.shoppinglist.ui.SharedListViewModel
import com.artf.shoppinglist.ui.shoppingListDialog.NewListDialog
import com.artf.shoppinglist.util.ShoppingListType
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CurrentListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sharedListViewModel: SharedListViewModel by activityViewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCurrentListBinding.inflate(LayoutInflater.from(context))
        binding.sharedListViewModel = sharedListViewModel
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = CurrentListAdapter(this, getListItemListener())

        sharedListViewModel.setShoppingListType(ShoppingListType.CURRENT)
        sharedListViewModel.createItem.observe(this, Observer {
            it?.let {
                val reviewDialog = NewListDialog()
                reviewDialog.show(parentFragmentManager, NewListDialog::class.simpleName)
                sharedListViewModel.onFabClicked(null)
            }
        })

        sharedListViewModel.shoppingListType.observe(this, Observer {
            if (isThisDestination().not()) return@Observer
            if (it != ShoppingListType.ARCHIVED) return@Observer
            findNavController().navigate(R.id.action_current_list_to_archived_list)
        })

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //menu.setGroupVisible(R.id.menuGroup, false)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getListItemListener(): CurrentListAdapter.ClickListenerInt {
        return object : CurrentListAdapter.ClickListenerInt {
            override fun onClickListenerButton(shoppingList: ShoppingList) {
                sharedListViewModel.updateShoppingList(shoppingList, true)
            }

            override fun onClickListenerRow(shoppingList: ShoppingList) {
                sharedListViewModel.onShoppingListClick(shoppingList)
                if (isThisDestination().not()) return
                findNavController().navigate(R.id.action_current_list_to_product_list)
            }
        }
    }

    fun isThisDestination(): Boolean {
        return findNavController().currentDestination?.id == R.id.fragment_current_list
    }

}