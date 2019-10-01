package com.artf.shoppinglist.ui.productDialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.artf.shoppinglist.R
import com.artf.shoppinglist.databinding.DialogNewProductBinding
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

class NewProductDialog : DaggerDialogFragment() {

    companion object {
        var shoppingListId = 0L
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val newProductViewModel: NewProductViewModel by viewModels { viewModelFactory }

    lateinit var binding: DialogNewProductBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogNewProductBinding.inflate(LayoutInflater.from(activity))
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
            val productQuantity = binding.quantityEditText.text.toString().toLong()
            val productName = binding.nameEditText.text.toString()
            if (productName.isEmpty()) {
                binding.nameEditText.error = activity!!.getString(R.string.product_empty)
                return@OnClickListener
            }

            newProductViewModel.createProduct(productName, productQuantity, shoppingListId)
            dialog?.dismiss()
        }
    }

}