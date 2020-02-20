package com.artf.shoppinglist.ui.currentList

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ArcMotion
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.artf.shoppinglist.R
import com.artf.shoppinglist.databinding.FragmentCurrentListBinding
import com.artf.shoppinglist.util.visibleItemsRange

object CurrentListAnim {

    private val colorEvaluator = ArgbEvaluator()
    private lateinit var binding: FragmentCurrentListBinding
    private val size by lazy {
        val lm = binding.recyclerView.layoutManager as LinearLayoutManager
        val pos = lm.findFirstVisibleItemPosition()
        val viewHolder = binding.recyclerView.findViewHolderForLayoutPosition(pos)
        val container = (viewHolder as CurrentListAdapter.MsgViewHolder).binding.container
        Pair(container.width, container.height)
    }

    fun animArcMotionFab(binding: FragmentCurrentListBinding) {
        val animArcMotionFab = getAnimArcMotionFab(binding)
        val animScaleDownRecyclerView = getAnimScaleDownRecyclerView(binding)

        val animRevealCard = getAnimRevealCard(binding)
        val animFadeOutFab = getAnimFadeOut(binding)

        val animTransitionXBottomBar = getAnimColoTranslationBottomBar(binding)
        val animColoTranslationBottomBar = getAnimTranslationBottomBar(binding)

        animArcMotionFab.doOnEnd {
            val set = AnimatorSet()
            set.playTogether(animRevealCard, animFadeOutFab)
            set.start()
        }

        animRevealCard.doOnEnd {
            val binding = binding.incContainer
            binding.bottomBarContainer.visibility = View.VISIBLE
            binding.fab3.visibility = View.INVISIBLE

            val set = AnimatorSet()
            set.playTogether(animTransitionXBottomBar, animColoTranslationBottomBar)
            set.start()
        }

        val set = AnimatorSet()
        set.playTogether(animArcMotionFab, animScaleDownRecyclerView)
        set.start()
    }

    private fun getAnimArcMotionFab(binding: FragmentCurrentListBinding): ObjectAnimator {
        val binding2 = binding.incContainer
        val x = ArcMotion().apply { maximumAngle = 90f }
        val endX = binding.fab.x
        val endY = binding.fab.y

        val startX = binding2.cardView.x + binding2.cardView.width / 2f - binding.fab.width / 2f
        val startY =
            binding2.cardViewContainer.y + binding2.cardViewContainer.height / 2 - binding.fab.height / 2f
        val path = x.getPath(endX, endY, startX, startY)
        val objectAnimator = ObjectAnimator.ofFloat(binding.fab, "x", "y", path)
        objectAnimator.duration = 400
        return objectAnimator
    }

    private fun getAnimRevealCard(binding: FragmentCurrentListBinding): ValueAnimator {
        val binding2 = binding.incContainer
        val card_width = binding2.cardView.width
        val card_height = binding2.cardView.height
        val revealIn = ObjectAnimator.ofFloat(0.3f, 1f)

        revealIn.interpolator = AccelerateDecelerateInterpolator()
        revealIn.addUpdateListener {
            val value = it.animatedValue as Float
            binding2.cardView.apply {
                layoutParams.width = (card_width * value).toInt()
                layoutParams.height = (card_height * value).toInt()
                val rad = 1 - value
                val x = if (rad > 0.01f) rad else 0f
                radius = layoutParams.width / 2f * x
                requestLayout()
            }
        }
        revealIn.doOnStart {
            binding2.bottomBarContainer.translationX = -card_width / 4f
            binding2.cardView.visibility = View.VISIBLE
        }
        revealIn.duration = 600
        return revealIn
    }

    private fun getAnimColoTranslationBottomBar(binding: FragmentCurrentListBinding): ObjectAnimator {
        val binding2 = binding.incContainer
        val objectAnimator = ObjectAnimator.ofObject(
            binding2.bottomBarContainer,
            "backgroundColor",
            colorEvaluator,
            ContextCompat.getColor(binding.root.context, R.color.colorPrimary),
            ContextCompat.getColor(binding.root.context, R.color.colorPrimaryDark)
        )
        objectAnimator.duration = 300
        return objectAnimator
    }

    private fun getAnimTranslationBottomBar(binding: FragmentCurrentListBinding): ValueAnimator {
        val binding2 = binding.incContainer

        val fadeOut = ObjectAnimator.ofFloat(
            binding2.bottomBarContainer,
            "translationX",
            -binding2.bottomBarContainer.width / 4f,
            0f
        )
        fadeOut.duration = 300
        return fadeOut
    }

    private fun getAnimFadeOut(binding: FragmentCurrentListBinding): ObjectAnimator {
        val fadeOut = ObjectAnimator.ofFloat(binding.fab, "alpha", 1f, 0f)
        fadeOut.duration = 1
        return fadeOut
    }

    private fun getAnimScaleDownRecyclerView(binding: FragmentCurrentListBinding): ValueAnimator {
        val scaleDown = ObjectAnimator.ofFloat(1f, 0.8f)
        val lm = binding.recyclerView.layoutManager as LinearLayoutManager
        this.binding = binding

        scaleDown.addUpdateListener {
            val value = it.animatedValue as Float
            for (i in lm.visibleItemsRange) {
                val viewHolder = binding.recyclerView.findViewHolderForLayoutPosition(i) ?: break
                val container = (viewHolder as CurrentListAdapter.MsgViewHolder).binding.container
                container.children.toList().map { view ->
                    view.scaleX = value
                    view.scaleY = value
                    view.alpha = 1f * value
                }
                container.layoutParams.width = (size.first * value).toInt()
                container.layoutParams.height = (size.second * value).toInt()
                container.alpha = 1f * value
                container.requestLayout()
            }
        }
        scaleDown.duration = 400
        return scaleDown
    }
}