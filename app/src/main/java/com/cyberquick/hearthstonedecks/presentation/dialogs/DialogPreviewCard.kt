package com.cyberquick.hearthstonedecks.presentation.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.DialogCardFullSizeBinding
import com.cyberquick.hearthstonedecks.presentation.adapters.CardFullSizeAdapter
import com.cyberquick.hearthstonedecks.presentation.common.entities.CardFullSizeData
import com.cyberquick.hearthstonedecks.utils.Event
import com.cyberquick.hearthstonedecks.utils.bold
import com.cyberquick.hearthstonedecks.utils.fromHtml
import com.cyberquick.hearthstonedecks.utils.logFirebaseEvent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import jp.wasabeef.blurry.Blurry

class DialogPreviewCard(
    context: Context,
    private val sourceScreen: View,
    private val cards: List<CardFullSizeData>,
    private val selectedCardIndex: Int,
    private val onClosed: () -> Unit,
) : Dialog(context, R.style.ImagePreviewerTheme) {

    private lateinit var binding: DialogCardFullSizeBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCardFullSizeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logFirebaseEvent(context, Event.CARDS_START_VIEWING)

        val screenshot = screenShot(sourceScreen.rootView)
        Blurry.with(context)
            .radius(5)  // default 25
            .sampling(1)    // default 1
            .from(screenshot).into(binding.blurredBackground)

        binding.blurredBackground.setOnClickListener {
            dismiss()
        }

        val bottomSheet = binding.bottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheet.setOnClickListener {
            bottomSheetBehavior.state = when (bottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_EXPANDED -> BottomSheetBehavior.STATE_COLLAPSED
                else -> BottomSheetBehavior.STATE_EXPANDED
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTexts(selectedId = position)
                logFirebaseEvent(context, Event.CARD_VIEW)
            }
        })

        binding.viewPager.offscreenPageLimit = cards.size
        binding.viewPager.adapter = CardFullSizeAdapter(
            onPreviousItemClicked = { instantOpenPage(binding.viewPager.currentItem - 1) },
            onNextItemClicked = { instantOpenPage(binding.viewPager.currentItem + 1) },
            onCenterClicked = { dismiss() },
        ).apply { set(cards) }

        instantOpenPage(selectedCardIndex)
        updateTexts(selectedId = selectedCardIndex)
    }

    @SuppressLint("SetTextI18n")
    private fun updateTexts(selectedId: Int) {
        val card = cards[selectedId].cardCountable.card
        val dataAboutSet = cards[selectedId].dataAboutSet

        val expansion = dataAboutSet.setName
        val year = dataAboutSet.year
        val flavor = card.flavorText
        val artist = card.artistName

        binding.expansion.text = (context.getString(R.string.expansion).bold() + ": " + expansion)
            .fromHtml()

        binding.year.isVisible = year != null
        year?.let { nonNullYear ->
            binding.year.text = (context.getString(R.string.year).bold() + ": " + nonNullYear)
                .fromHtml()
        }

        binding.flavor.text = (context.getString(R.string.flavor).bold() + ": " + flavor)
            .fromHtml()
        binding.artist.text = (context.getString(R.string.artist).bold() + ": " + artist)
            .fromHtml()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onClosed()
    }

    private fun instantOpenPage(index: Int) {
        binding.viewPager.setCurrentItem(index, false)
    }

    private fun screenShot(view: View): Bitmap? {
        fun pixelsOfIdentifier(name: String, defaultDp: Int): Int {
            val resources = context.resources
            val resourceId = resources.getIdentifier(name, "dimen", "android")
            return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
            else (defaultDp * resources.displayMetrics.density).toInt()
        }

        val width = view.width
        val height = view.height

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        val cutTop = pixelsOfIdentifier("status_bar_height", 24)
        val cutBottom = pixelsOfIdentifier("navigation_bar_height", 48)
        val totalHeight = height - cutTop - cutBottom
        val cutBitmap = Bitmap.createBitmap(bitmap, 0, cutTop, width, totalHeight)

        val scaleRatio = 6
        return Bitmap.createScaledBitmap(
            cutBitmap,
            cutBitmap.width / scaleRatio,
            cutBitmap.height / scaleRatio,
            false
        )
    }
}