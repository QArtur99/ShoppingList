package com.artf.shoppinglist.ui.productDialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.artf.shoppinglist.R
import com.artf.shoppinglist.databinding.DialogNewProductBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewProductDialog : DialogFragment() {

    companion object {
        var shoppingListId = 0L
    }

    private val newProductViewModel: NewProductViewModel by viewModel()

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
            val productName = binding.nameEditText.text.toString()
            if (productName.isEmpty()) {
                binding.nameEditText.error = activity!!.getString(R.string.product_empty)
                return@OnClickListener
            }

            val pqString = binding.quantityEditText.text.toString()
            val productQuantity = if (pqString.isEmpty()) 1L else pqString.toLong()
            newProductViewModel.createProduct(productName, productQuantity, shoppingListId)
            dialog?.dismiss()
        }
    }
}