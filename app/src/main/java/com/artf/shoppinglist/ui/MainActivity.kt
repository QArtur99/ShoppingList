package com.artf.shoppinglist.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.artf.shoppinglist.R
import com.artf.shoppinglist.databinding.ActivityMainBinding
import com.artf.shoppinglist.util.ShoppingListType
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val sharedViewModel: SharedViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.current_shopping_list -> sharedViewModel.setShoppingListType(ShoppingListType.CURRENT)
            R.id.archived_shopping_list -> sharedViewModel.setShoppingListType(ShoppingListType.ARCHIVED)
        }
        return true
    }
}
