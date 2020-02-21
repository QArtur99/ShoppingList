package com.artf.shoppinglist.ui.currentList

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ArcMotion
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.artf.shoppinglist.R
import com.artf.shoppinglist.databinding.FragmentCurrentListBinding
import com.artf.shoppinglist.databinding.IncludeCardBinding
import com.artf.shoppinglist.util.visibleItemsRange

class CurrentListAnim(
    private val binding: FragmentCurrentListBinding,
    private val binding2: IncludeCardBinding
) {

    private val fab by lazy {
        val fab = binding.fab
        arrayOf(fab.x, fab.y, fab.width.toFloat(), fab.height.toFloat())
    }
    private val card by lazy {
        val fab = binding2.cardView
        arrayOf(fab.x, fab.y, fab.width.toFloat(), fab.height.toFloat())
    }

    private val colorEvaluator = ArgbEvaluator()
    private val size by lazy {
        val lm = binding.recyclerView.layoutManager as LinearLayoutManager
        val pos = lm.findFirstVisibleItemPosition()
        val viewHolder = binding.recyclerView.findViewHolderForLayoutPosition(pos)
        val container = (viewHolder as CurrentListAdapter.MsgViewHolder).binding.container
        Pair(container.width, container.height)
    }

    fun animArcMotionFab() {
        val animArcMotionFab = getAnimArcMotionFab(false)
        val animScaleDownRecyclerView = getAnimScaleDownRecyclerView(false)

        val animRevealCard = getAnimRevealCard(false)
        val animFadeOutFab = getAnimFadeOut(false)

        val animTransitionXBottomBar = getAnimColoTranslationBottomBar(false)
        val animColoTranslationBottomBar = getAnimTranslationBottomBar(false)
        val animTranslationTopmBar = getAnimTranslationTopmBar(false)

        animArcMotionFab.doOnEnd {
            val set = AnimatorSet()
            set.playTogether(animRevealCard, animFadeOutFab)
            set.start()
        }

        animRevealCard.doOnStart {
            binding2.bottomBarContainer.translationX = -binding2.cardView.width / 4f
            binding2.cardView.visibility = View.VISIBLE
        }

        animRevealCard.doOnEnd {
            binding2.bottomBarContainer.visibility = View.VISIBLE
            binding2.cardTitle.visibility = View.VISIBLE
            binding2.fab3.visibility = View.INVISIBLE

            val set = AnimatorSet()
            set.playTogether(
                animTransitionXBottomBar,
                animColoTranslationBottomBar,
                animTranslationTopmBar
            )
            set.start()
        }

        animTranslationTopmBar.doOnEnd {
            binding2.cardTitle.elevation = 8f
            binding2.bottomBarContainer.elevation = 8f
        }

        val set = AnimatorSet()
        set.playTogether(animArcMotionFab, animScaleDownRecyclerView)
        set.start()
    }

    fun animArcMotionFabRevers() {
        val animArcMotionFab = getAnimArcMotionFab(true)
        val animScaleDownRecyclerView = getAnimScaleDownRecyclerView(true)

        val animRevealCard = getAnimRevealCard(true)
        val animFadeOutFab = getAnimFadeOut(true)

        val animTransitionXBottomBar = getAnimColoTranslationBottomBar(true)
        val animColoTranslationBottomBar = getAnimTranslationBottomBar(true)
        val animTranslationTopmBar = getAnimTranslationTopmBar(true)

        animFadeOutFab.doOnEnd {
            val set = AnimatorSet()
            set.playTogether(animScaleDownRecyclerView, animArcMotionFab)
            set.start()
        }

        animRevealCard.doOnEnd {
            binding2.bottomBarContainer.translationX = 0f
            binding2.cardView.visibility = View.INVISIBLE

            val set = AnimatorSet()
            set.playTogether(animFadeOutFab)
            set.start()
        }

        animTranslationTopmBar.doOnEnd {
            binding2.bottomBarContainer.visibility = View.INVISIBLE
            binding2.cardTitle.visibility = View.INVISIBLE
            binding2.fab3.visibility = View.VISIBLE

            val set = AnimatorSet()
            set.playTogether(animRevealCard)
            set.start()
        }

        animTranslationTopmBar.doOnStart {
            binding2.cardTitle.elevation = 0f
            binding2.bottomBarContainer.elevation = 0f
        }

        val set = AnimatorSet()
        set.playTogether(
            animTransitionXBottomBar,
            animColoTranslationBottomBar,
            animTranslationTopmBar
        )
        set.start()
    }

    private fun getAnimArcMotionFab(isRevers: Boolean): ObjectAnimator {
        val cardView = binding2.cardViewContainer
        val container = binding2.cardViewContainer

        val x = ArcMotion().apply { maximumAngle = 90f }
        val endX = fab[0]
        val endY = fab[1]
        val startX = cardView.x + cardView.width / 2f - fab[2] / 2f
        val startY = container.y + container.height / 2 - fab[3] / 2f
        val path = when (isRevers) {
            true -> x.getPath(startX, startY, endX, endY)
            false -> x.getPath(endX, endY, startX, startY)
        }
        return ObjectAnimator.ofFloat(binding.fab, "x", "y", path).apply {
            duration = 400
        }
    }

    private fun getAnimRevealCard(isRevers: Boolean): ValueAnimator {
        val forwardX = 0.2f
        val forwardY = 1f
        val value = if (isRevers) Pair(forwardY, forwardX) else Pair(forwardX, forwardY)
        return ObjectAnimator().apply {
            duration = 600
            interpolator = AccelerateDecelerateInterpolator()
            setFloatValues(value.first, value.second)
            addUpdateListener { resizeCardView(it.animatedValue as Float) }
        }
    }

    private fun resizeCardView(newValue: Float) {
        binding2.cardView.apply {
            layoutParams.width = (card[2] * newValue).toInt()
            layoutParams.height = (card[3] * newValue).toInt()
            val rad = 1 - newValue
            val x = if (rad > 0.01f) rad else 0f
            radius = layoutParams.width / 2f * x
            requestLayout()
        }
    }

    private fun getAnimColoTranslationBottomBar(isRevers: Boolean): ObjectAnimator {
        val forwardX = ContextCompat.getColor(binding.root.context, R.color.colorPrimary)
        val forwardY = ContextCompat.getColor(binding.root.context, R.color.colorPrimaryDark)
        val value = if (isRevers) Pair(forwardY, forwardX) else Pair(forwardX, forwardY)

        return ObjectAnimator().apply {
            target = binding2.bottomBarContainer
            duration = 300
            setPropertyName("backgroundColor")
            setIntValues(value.first, value.second)
            setEvaluator(colorEvaluator)
        }
    }

    private fun getAnimTranslationBottomBar(isRevers: Boolean): ValueAnimator {
        val forwardX = -binding2.bottomBarContainer.width / 4f
        val forwardY = 0f
        val value = if (isRevers) Pair(forwardY, forwardX) else Pair(forwardX, forwardY)

        return ObjectAnimator().apply {
            target = binding2.bottomBarContainer
            duration = 300
            setPropertyName("translationX")
            setFloatValues(value.first, value.second)
        }
    }

    private fun getAnimTranslationTopmBar(isRevers: Boolean): ValueAnimator {
        val forwardX = 0f
        val forwardY = binding2.cardTitle.height * -1f
        val value = if (isRevers) Pair(forwardY, forwardX) else Pair(forwardX, forwardY)

        return ObjectAnimator().apply {
            target = binding2.cardTitle
            duration = 300
            setPropertyName("translationY")
            setFloatValues(value.first, value.second)
        }
    }

    private fun getAnimFadeOut(isRevers: Boolean): ObjectAnimator {
        val forwardX = 1f
        val forwardY = 0f
        val value = if (isRevers) Pair(forwardY, forwardX) else Pair(forwardX, forwardY)

        return ObjectAnimator().apply {
            target = binding.fab
            duration = 1
            setPropertyName("alpha")
            setFloatValues(value.first, value.second)
        }
    }

    private fun getAnimScaleDownRecyclerView(isRevers: Boolean): ValueAnimator {
        val forwardX = 1f
        val forwardY = 0.8f
        val value = if (isRevers) Pair(forwardY, forwardX) else Pair(forwardX, forwardY)

        return ObjectAnimator().apply {
            duration = 400
            setFloatValues(value.first, value.second)
            addUpdateListener { resizeRecyclerViewItems(it.animatedValue as Float) }
        }
    }

    private fun resizeRecyclerViewItems(newValue: Float) {
        val lm = binding.recyclerView.layoutManager as LinearLayoutManager
        for (i in lm.visibleItemsRange) {
            val container = getHolderContainer(i) ?: break
            scaleChildren(container, newValue)
            container.layoutParams.width = (size.first * newValue).toInt()
            container.layoutParams.height = (size.second * newValue).toInt()
            container.alpha = 1f * newValue
            container.requestLayout()
        }
    }

    private fun scaleChildren(container: RelativeLayout, newValue: Float) {
        container.children.toList().map { view ->
            view.scaleX = newValue
            view.scaleY = newValue
            view.alpha = 1f * newValue
        }
    }

    private fun getHolderContainer(pos: Int): RelativeLayout? {
        val viewHolder = binding.recyclerView.findViewHolderForLayoutPosition(pos) ?: return null
        return (viewHolder as CurrentListAdapter.MsgViewHolder).binding.container
    }
}