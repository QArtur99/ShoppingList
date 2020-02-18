package com.artf.shoppinglist.ui.currentList

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ArcMotion
import com.artf.shoppinglist.R
import com.artf.shoppinglist.database.ShoppingList
import com.artf.shoppinglist.databinding.FragmentCurrentListBinding
import com.artf.shoppinglist.ui.SharedViewModel
import com.artf.shoppinglist.ui.shoppingListDialog.NewListDialog
import com.artf.shoppinglist.util.ShoppingListType
import com.artf.shoppinglist.util.visibleItemsRange
import org.koin.androidx.viewmodel.ext.android.viewModel

open class CurrentListFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by viewModel()

    lateinit var binding: FragmentCurrentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentListBinding.inflate(LayoutInflater.from(context))
        binding.sharedViewModel = sharedViewModel
        binding.lifecycleOwner = this
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
            animArcMotionFab()
        }

        return binding.root
    }

    private fun animArcMotionFab() {
        val x = ArcMotion().apply { maximumAngle = 90f }
        val endX = binding.fab.x
        val endY = binding.fab.y
        val startX = binding.cardView.x + binding.cardView.width / 2f - binding.fab.width / 2f
        val startY = binding.cardViewContainer.y + binding.cardViewContainer.height/2 - binding.fab.height / 2f
        val path = x.getPath(endX, endY, startX, startY)
        val objectAnimator = ObjectAnimator.ofFloat(binding.fab, "x", "y", path)
        objectAnimator.duration = 300
        // objectAnimator.start()
        objectAnimator.doOnEnd {
            // circularRevealIn(binding.cardView, 600, binding.fab.width / 2f)
            //animFadeOut()
        }
        val card_width = binding.cardView.width
        val card_height = binding.cardView.height
        val revealIn = ObjectAnimator.ofFloat(0.2f, 1f)
        revealIn.addUpdateListener {
            val value = it.animatedValue as Float
            binding.cardView.apply {
                layoutParams.width = (card_width * value).toInt()
                layoutParams.height = (card_height * value).toInt()
                radius = layoutParams.width/2f * (1 - value)
                requestLayout()
            }
        }
        revealIn.doOnStart { binding.cardView.visibility = View.VISIBLE }
        revealIn.duration = 600
        // revealIn.start()
        val set = AnimatorSet()
        set.play(objectAnimator)
        set.play(revealIn).after(objectAnimator)
        set.start()
        // animScaleDownRecyclerView()
    }

    private fun animFadeOut() {
        val fadeOut = ObjectAnimator.ofFloat(binding.fab, "alpha", 1f, 0f)
        fadeOut.duration = 300
        fadeOut.start()
    }

    private fun animScaleDownRecyclerView() {
        val scaleDown = ObjectAnimator.ofFloat(1f, 0.8f)
        val lm = binding.recyclerView.layoutManager as LinearLayoutManager

        scaleDown.addUpdateListener {
            val value = it.animatedValue as Float
            for (i in lm.visibleItemsRange) {
                val holder =
                    binding.recyclerView.findViewHolderForLayoutPosition(i) as CurrentListAdapter.MsgViewHolder
                val container = holder.binding.container
                container.layoutParams.width = (700 * value).toInt()
                container.layoutParams.height = (150 * value).toInt()
                container.alpha = 1f * value
                holder.itemView.requestLayout()
            }
        }
        scaleDown.duration = 600
        scaleDown.start()
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