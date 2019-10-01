package com.artf.shoppinglist.ui.currentList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.databinding.FragmentCurrentListBinding
import com.artf.shoppinglist.ui.NewListDialog
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CurrentListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val currentListViewModel: CurrentListViewModel by activityViewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCurrentListBinding.inflate(LayoutInflater.from(context))
        binding.currentListViewModel = currentListViewModel
        binding.lifecycleOwner = this

        binding.recyclerView.adapter =
            CurrentListAdapter(this, getListItemListener())

        currentListViewModel.createShoppingList.observe(this, Observer {
            it?.let {
                val reviewDialog = NewListDialog()
                reviewDialog.show(requireFragmentManager(), NewListDialog::class.simpleName)
                currentListViewModel.onFabClicked(null)
            }
        })

        return binding.root
    }


    private fun getListItemListener(): CurrentListAdapter.ClickListenerInt {
        return object : CurrentListAdapter.ClickListenerInt {
            override fun onClickListenerButton(productId: ShoppingList) {

            }

            override fun onClickListenerRow(productId: ShoppingList) {

            }
        }
    }

}