package com.artf.shoppinglist.ui.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.artf.shoppinglist.R
import com.artf.shoppinglist.databinding.ActivityMainBinding
import com.artf.shoppinglist.util.ShoppingListType
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sharedViewModel: SharedViewModel by viewModels { viewModelFactory }
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
