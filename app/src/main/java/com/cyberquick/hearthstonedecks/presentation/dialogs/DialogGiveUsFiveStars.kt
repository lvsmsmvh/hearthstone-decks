package com.cyberquick.hearthstonedecks.presentation.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.DialogRateAppBinding

class DialogGiveUsFiveStars(
    private val activity: Activity,
    private val onYesClick: () -> Unit,
    private val onNoClick: () -> Unit,
) {

    fun show() {
        val binding = DialogRateAppBinding.inflate(activity.layoutInflater)

        val dialog = AlertDialog.Builder(activity, R.style.RateAppDialog)
            .setView(binding.root)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))!!
        dialog.show()

        dialog.setOnDismissListener {
            onNoClick()
        }

        binding.tvGiveUsFiveStars.text = activity.getString(R.string.give_us_five_stars)
            .formatForEmoji()

        binding.tvYourReview.text = activity.getString(R.string.your_review)
            .formatForEmoji()

        binding.btnNo.setOnClickListener {
            dialog.setOnDismissListener { }
            dialog.dismiss()
            onNoClick()
        }

        binding.btnYes.setOnClickListener {
            dialog.setOnDismissListener { }
            dialog.dismiss()
            onYesClick()
        }

        binding.lottieAnim.setOnClickListener {
            dialog.setOnDismissListener { }
            dialog.dismiss()
            onYesClick()
        }
    }

    private fun String.formatForEmoji(): String {
        if (!this.contains("uni-")) return this
        val indexOfUni = this.indexOf("uni-")
        val uniLength = 9   // uni-1F628, uni-1F60A -- 9 symbols
        val uni = this.substring(indexOfUni, indexOfUni + uniLength)
        val emojiCodeInt = uni.substring(4).toInt(16)
        val emojiFormatted = String(Character.toChars(emojiCodeInt))
        return this.replace(uni, emojiFormatted)
    }
}