package com.artf.shoppinglist.ui.shoppingListDialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.artf.shoppinglist.R
import com.artf.shoppinglist.databinding.DialogNewListBinding
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

class NewListDialog : DaggerDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val newListViewModel: NewListViewModel by viewModels { viewModelFactory }

    lateinit var binding: DialogNewListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogNewListBinding.inflate(LayoutInflater.from(activity))
        binding.cancelButton.setOnClickListener(getCancelButtonClickListener())
        binding.addButton.setOnClickListener(getAddButtonClickListener())

        dialog?.window?.decorView?.background =
            ColorDrawable(ContextCompat.getColor(context!!, R.color.colorTransparent))
        return binding.root
    }

    private fun getCancelButtonClickListener(): View.OnClickListener {
        return View.OnClickListener { dialog?.dismiss() }
    }

    private fun getAddButtonClickListener(): View.OnClickListener {
        return View.OnClickListener {
            val shoppingListName = binding.dialogEditText.text.toString()
            if (shoppingListName.isEmpty()) {
                binding.dialogEditText.error = activity!!.getString(R.string.shopping_list_empty)
                return@OnClickListener
            }
            newListViewModel.createShoppingList(shoppingListName)
            dialog?.dismiss()
        }
    }
}