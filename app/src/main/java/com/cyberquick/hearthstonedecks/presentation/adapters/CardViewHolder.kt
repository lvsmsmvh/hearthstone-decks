package com.cyberquick.hearthstonedecks.presentation.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.DialogCardFullSizeBinding
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.utils.drawable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import jp.wasabeef.blurry.Blurry

class CardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    @SuppressLint("SetTextI18n")
    fun bind(cardCountable: CardAdapter.CardCountable, width: Int) {
        val imageSmall = view.findViewById<ImageView>(R.id.img_card_small)

        Picasso.with(view.context)
            .load(cardCountable.card.image)
            .fit()
            .centerInside()
            .into(imageSmall)

        view.findViewById<TextView>(R.id.tv_amount).text = "x" + cardCountable.amount

        imageSmall.setOnClickListener {
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
                .fit()
                .centerInside()
                .into(binding.previewerImage)

            val screenshot = screenShot(source.rootView)
            binding.blurredBackground.setImageDrawable(screenshot?.toDrawable(context.resources))
            binding.blurredBackground.setOnClickListener {
                dismiss()
            }
            Blurry.with(context).from(screenshot).into(binding.blurredBackground)

            binding.quote.text = context.getString(R.string.quote_ph, card.flavorText)
            binding.author.text = context.getString(R.string.artist_name_ph, card.artistName)

            val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
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
            val cutTop = pixelsOfIdentifier("status_bar_height", 24)
            val cutBottom = pixelsOfIdentifier("navigation_bar_height", 48)
            val totalHeight = height - cutTop - cutBottom

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)

            return Bitmap.createBitmap(bitmap, 0, cutTop, width, totalHeight)
        }
    }
}
