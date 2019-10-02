package com.artf.shoppinglist.ui.archivedList

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
import com.artf.shoppinglist.databinding.FragmentArchivedListBinding
import com.artf.shoppinglist.ui.MainActivity
import com.artf.shoppinglist.ui.SharedViewModel
import com.artf.shoppinglist.util.ShoppingListType
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ArchivedListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sharedViewModel: SharedViewModel by activityViewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentArchivedListBinding.inflate(LayoutInflater.from(context))
        binding.sharedViewModel = sharedViewModel
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = ArchivedListAdapter(getListItemListener())

        sharedViewModel.setShoppingListType(ShoppingListType.ARCHIVED)
        sharedViewModel.shoppingListType.observe(this, Observer {
            if (isThisDestination().not()) return@Observer
            if (it != ShoppingListType.CURRENT) return@Observer
            findNavController().navigate(R.id.action_archived_list_to_current_list)
        })

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        return binding.root
    }


    private fun getListItemListener(): ArchivedListAdapter.ClickListenerInt {
        return object : ArchivedListAdapter.ClickListenerInt {
            override fun onClickListenerButton(shoppingList: ShoppingList) {
                sharedViewModel.updateShoppingList(shoppingList, false)
            }

            override fun onClickListenerRow(shoppingList: ShoppingList) {
                sharedViewModel.onShoppingListClick(shoppingList)
                if (isThisDestination().not()) return
                findNavController().navigate(R.id.action_archived_list_to_product_list)
            }
        }
    }

    fun isThisDestination(): Boolean {
        return findNavController().currentDestination?.id == R.id.fragment_archived_list
    }

}