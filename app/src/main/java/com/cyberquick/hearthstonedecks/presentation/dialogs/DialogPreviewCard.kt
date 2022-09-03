package com.cyberquick.hearthstonedecks.presentation.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.DialogCardFullSizeBinding
import com.cyberquick.hearthstonedecks.domain.entities.CardCountable
import com.cyberquick.hearthstonedecks.presentation.adapters.CardFullSizeAdapter
import com.cyberquick.hearthstonedecks.presentation.common.entities.CardFullSizeData
import com.cyberquick.hearthstonedecks.utils.color
import jp.wasabeef.blurry.Blurry

class DialogPreviewCard(
    context: Context,
    private val sourceScreen: View,
    private val cards: List<CardFullSizeData>,
    private val selectedCard: CardCountable,
    private val onClosed: () -> Unit,
) : Dialog(context, R.style.ImagePreviewerTheme) {

    private lateinit var binding: DialogCardFullSizeBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCardFullSizeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val screenshot = screenShot(sourceScreen.rootView)
        Blurry.with(context)
            .radius(5)  // default 25
            .sampling(1)    // default 1
            .from(screenshot).into(binding.blurredBackground)

        binding.blurredBackground.setOnClickListener {
            dismiss()
        }

        repeat(cards.size) {
            layoutInflater.inflate(R.layout.item_view_pager_indicator, binding.viewPagerIndicators)
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            private var currentSelectedPage = 0
            set(value) {
                updatePosition(field, false)
                updatePosition(value, true)
                field = value
            }

            override fun onPageSelected(position: Int) {
                currentSelectedPage = position
            }

            private fun updatePosition(position: Int, isSelected: Boolean) {
                val tintRes = if (isSelected) R.color.palette_text_1 else R.color.palette_950
                (binding.viewPagerIndicators[position] as ImageView).setColorFilter(
                    context.color(tintRes), PorterDuff.Mode.SRC_IN
                )
            }
        })

        binding.viewPager.adapter = CardFullSizeAdapter().apply { set(cards) }
        val selectedCardIndex = cards.indexOfFirst { it.cardCountable == selectedCard }
        binding.viewPager.setCurrentItem(selectedCardIndex, false)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onClosed()
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