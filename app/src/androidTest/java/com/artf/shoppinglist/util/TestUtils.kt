package com.artf.shoppinglist.util

import android.view.View
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.hamcrest.TypeSafeMatcher
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun withIndex(matcher: Matcher<View>, index: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        internal var currentIndex = 0

        override fun describeTo(description: Description) {
            description.appendText("with index: ")
            description.appendValue(index)
            matcher.describeTo(description)
        }

        override fun matchesSafely(view: View): Boolean {
            return matcher.matches(view) && currentIndex++ == index
        }
    }
}

fun waitForAdapterChange(recyclerView: RecyclerView, countingRule: CountingTaskExecutorRule) {
    val latch = CountDownLatch(1)
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
        recyclerView.adapter?.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    latch.countDown()
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    latch.countDown()
                }
            })
    }
    countingRule.drainTasks(1, TimeUnit.SECONDS)
    if (recyclerView.adapter?.itemCount ?: 0 > 0) {
        return
    }
    MatcherAssert.assertThat(latch.await(100, TimeUnit.SECONDS), CoreMatchers.`is`(true))
}

fun clickChildViewWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Click on a child view with specified id."
        }

        override fun perform(uiController: UiController?, view: View) {
            val v = view.findViewById<View>(id)
            v.performClick()
        }
    }
}