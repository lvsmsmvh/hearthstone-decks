package com.cyberquick.hearthstonedecks.presentation.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.DialogCardFullSizeBinding
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.utils.drawable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import jp.wasabeef.blurry.Blurry

class CardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    @SuppressLint("SetTextI18n")
    fun bind(
        cardCountable: CardAdapter.CardCountable,
    ) {
        val imageSmall = view.findViewById<ImageView>(R.id.img_card_small)

        Picasso.with(view.context)
            .load(cardCountable.card.image)
            .error(R.drawable.ic_failed)
            .fit()
            .centerInside()
            .into(imageSmall)

        view.findViewById<TextView>(R.id.tv_amount).text = "x" + cardCountable.amount

        imageSmall.setOnClickListener {
            if ((bindingAdapter as CardAdapter).clicksBlocked.getAndSet(true)) {
                return@setOnClickListener
            }

            DialogPreviewCard(
                card = cardCountable.card,
                source = imageSmall,
                placeholder = try {
                    imageSmall.drawable.constantState?.newDrawable()
                } catch (e: NullPointerException) {
                    itemView.context.drawable(R.drawable.card_placeholder)
                },
            ).show()
        }
    }

    inner class DialogPreviewCard(
        val card: Card,
        val source: View,
        val placeholder: Drawable?,
    ) : Dialog(view.context, R.style.ImagePreviewerTheme) {

        private lateinit var binding: DialogCardFullSizeBinding

        @SuppressLint("ClickableViewAccessibility")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = DialogCardFullSizeBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.previewerImagePlaceholder.setImageDrawable(placeholder)
            Picasso.with(view.context)
                .load(card.image)
                .error(R.drawable.ic_failed)
                .fit()
                .centerInside()
                .into(binding.previewerImage)

            val screenshot = screenShot(source.rootView)
            Blurry.with(context)
                .radius(5)  // default 25
                .sampling(1)    // default 1
                .from(screenshot).into(binding.blurredBackground)
            binding.quote.text = context.getString(R.string.quote_ph, card.flavorText)
            binding.author.text = context.getString(R.string.artist_name_ph, card.artistName)
            binding.blurredBackground.setOnClickListener {
                dismiss()
            }

            val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
            binding.bottomSheet.setOnClickListener {
                bottomSheetBehavior.state = when (bottomSheetBehavior.state) {
                    BottomSheetBehavior.STATE_EXPANDED -> BottomSheetBehavior.STATE_COLLAPSED
                    else -> BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

        override fun onDetachedFromWindow() {
            super.onDetachedFromWindow()
            (bindingAdapter as CardAdapter).clicksBlocked.set(false)
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
}
