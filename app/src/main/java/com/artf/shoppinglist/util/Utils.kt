package com.artf.shoppinglist.util

import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnRepeat
import androidx.core.animation.doOnStart
import androidx.recyclerview.widget.LinearLayoutManager
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.math.hypot

enum class ShoppingListType { CURRENT, ARCHIVED }

fun getDateFormat(): SimpleDateFormat {
    val df = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    df.timeZone = TimeZone.getTimeZone("UTC")
    return df
}

fun circularRevealIn(view: View, time: Long, startRadius: Float) {
    val cx = view.width / 2
    val cy = view.height / 2
    val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
    val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, finalRadius)
    anim.duration = time
    anim.doOnStart { view.visibility = View.VISIBLE }
    anim.start()
}

fun circularRevealOut(view: View, time: Long, doOnEnd: () -> Unit) {
    val cx = view.width / 2
    val cy = view.height / 2
    val initialRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
    val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0f)
    anim.doOnEnd { doOnEnd.invoke() }
    anim.duration = time
    anim.start()
}


inline val LinearLayoutManager.visibleItemsRange: IntRange
    get() = findFirstVisibleItemPosition()..findLastVisibleItemPosition()