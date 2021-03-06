package com.artf.shoppinglist.ui

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import com.artf.shoppinglist.R
import com.artf.shoppinglist.testing.SingleFragmentActivity
import com.artf.shoppinglist.ui.data.model.ShoppingListUi
import com.artf.shoppinglist.ui.view.SharedViewModel
import com.artf.shoppinglist.ui.view.currentList.CurrentListFragment
import com.artf.shoppinglist.util.LiveDataTestUtil.getValueUI
import com.artf.shoppinglist.util.ShoppingListType
import com.artf.shoppinglist.util.ViewModelUtil
import com.artf.shoppinglist.util.clickChildViewWithId
import com.artf.shoppinglist.util.mock
import com.artf.shoppinglist.util.waitForAdapterChange
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.verify
import org.robolectric.annotation.LooperMode
import org.robolectric.annotation.TextLayoutMode

@RunWith(AndroidJUnit4::class)
@MediumTest
@LooperMode(LooperMode.Mode.PAUSED)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
@ExperimentalCoroutinesApi
class CurrentListFragmentTest {

    @Rule
    @JvmField
    var countingRule = CountingTaskExecutorRule()

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    private val sharedViewModel: SharedViewModel = Mockito.mock(
        SharedViewModel::class.java
    )
    private val currentListFragment = TestCurrentListFragment(sharedViewModel)

    private val shoppingLists = MutableLiveData<List<ShoppingListUi>>()
    private val createItem = MutableLiveData<Boolean>()
    private val shoppingListType = MutableLiveData<ShoppingListType>()

    private val shoppingListsList = mutableListOf<ShoppingListUi>().apply {
        add(ShoppingListUi(0L, "0", 0L, false))
        add(ShoppingListUi(1L, "1", 0L, false))
        add(ShoppingListUi(2L, "2", 0L, false))
    }

    @Before
    fun init() {
        `when`(sharedViewModel.shoppingListsUi).thenReturn(shoppingLists)
        `when`(sharedViewModel.createItem).thenReturn(createItem)
        `when`(sharedViewModel.shoppingListType).thenReturn(shoppingListType)
        doAnswer { createItem.postValue(null) }.`when`(sharedViewModel).onFabClicked(null)

        currentListFragment.viewModelFactory = ViewModelUtil.createFor(sharedViewModel)
        activityRule.activity.setFragment(currentListFragment)
    }

    @Test
    fun displayRecyclerView() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        shoppingLists.postValue(shoppingListsList)
        waitForAdapterChange(currentListFragment.binding.recyclerView, countingRule)
        onView(withId(R.id.recyclerView)).check(matches(hasChildCount(3)))
    }

    @Test
    fun onFabButtonClick() {
        onView(withId(R.id.fab)).perform(click())
        verify(sharedViewModel).onFabClicked(true)
    }

    @Test
    fun displayNewItemDialog() {
        createItem.postValue(true)
        assertThat(getValueUI(createItem, activityRule.activity)).isNull()
        onView(withId(R.id.titleDialog)).check(matches(isDisplayed()))
    }

    @Test
    fun itemClick() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        shoppingLists.postValue(shoppingListsList)
        waitForAdapterChange(currentListFragment.binding.recyclerView, countingRule)
        onView(withId(R.id.recyclerView)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
        verify(sharedViewModel).onShoppingListClick(null)
        assertThat(currentListFragment.isThisDestination()).isTrue()
        verify(currentListFragment.navController).navigate(R.id.action_current_list_to_product_list)
    }

    @Test
    fun itemArchiveButtonClick() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        shoppingLists.postValue(shoppingListsList)
        waitForAdapterChange(currentListFragment.binding.recyclerView, countingRule)
        onView(withId(R.id.recyclerView)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, clickChildViewWithId(R.id.imageButton)
            )
        )
        verify(sharedViewModel).updateShoppingList(shoppingListsList[0], true)
    }

    class TestCurrentListFragment(private val sharedViewModel: SharedViewModel) :
        CurrentListFragment() {
        val navController = mock<NavController>()
        override fun isThisDestination() = true
        override fun navController() = navController
        override fun viewModelFactory() = ViewModelUtil.createFor(sharedViewModel)
    }
}
