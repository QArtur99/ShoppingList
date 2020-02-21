package com.artf.shoppinglist.ui.currentList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.artf.shoppinglist.R
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.databinding.FragmentCurrentListBinding
import com.artf.shoppinglist.ui.SharedViewModel
import com.artf.shoppinglist.ui.shoppingListDialog.NewListDialog
import com.artf.shoppinglist.util.ShoppingListType
import org.koin.androidx.viewmodel.ext.android.viewModel

open class CurrentListFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by viewModel()

    lateinit var binding: FragmentCurrentListBinding
    lateinit var anim: CurrentListAnim

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentListBinding.inflate(LayoutInflater.from(context))
        binding.sharedViewModel = sharedViewModel
        binding.lifecycleOwner = this
        anim = CurrentListAnim(binding, binding.incContainer)

        binding.recyclerView.adapter = CurrentListAdapter(getListItemListener())

        sharedViewModel.setShoppingListType(ShoppingListType.CURRENT)
        sharedViewModel.createItem.observe(viewLifecycleOwner, Observer {
            it?.let {
                val reviewDialog = NewListDialog()
                reviewDialog.show(parentFragmentManager, NewListDialog::class.simpleName)
                sharedViewModel.onFabClicked(null)
            }
        })

        sharedViewModel.shoppingListType.observe(viewLifecycleOwner, Observer {
            if (isThisDestination().not()) return@Observer
            if (it != ShoppingListType.ARCHIVED) return@Observer
            navController().navigate(R.id.action_current_list_to_archived_list)
        })

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)

        binding.fab.setOnClickListener {
            //sharedViewModel.onFabClicked(true)
            anim.animArcMotionFab()
        }

        binding.incContainer.filterIcon.setOnClickListener {
            anim.animArcMotionFabRevers()
        }

        return binding.root
    }

    private fun getListItemListener(): CurrentListAdapter.ClickListenerInt {
        return object : CurrentListAdapter.ClickListenerInt {
            override fun onClickListenerButton(shoppingList: ShoppingList) {
                sharedViewModel.updateShoppingList(shoppingList, true)
            }

            override fun onClickListenerRow(shoppingList: ShoppingList) {
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
}