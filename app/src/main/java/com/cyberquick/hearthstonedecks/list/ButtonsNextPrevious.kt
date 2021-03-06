package com.cyberquick.hearthstonedecks.list

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.cyberquick.hearthstonedecks.R

class ButtonsNextPrevious {
    companion object {
        enum class Type(
            private val btnId: Int,
            private val viewPressedStateBgId: Int,
            private val textBtnId: Int
        ) {
            Previous(R.id.btn_previous, R.id.view_pressed_state_bg_previous, R.id.tv_btn_previous),
            Next(R.id.btn_next, R.id.view_pressed_state_bg_next, R.id.tv_btn_next);

            fun getButton(context: Context): FrameLayout{ return (context as FragmentActivity).findViewById(btnId) }
            fun getView(context: Context): View{ return (context as FragmentActivity).findViewById(viewPressedStateBgId) }
            fun getText(context: Context): TextView{ return (context as FragmentActivity).findViewById(textBtnId) }
        }

        fun makeButtonUnclickable(type: Type, context: Context?){
            if (context != null) {
                val button = type.getButton(context)
                val viewThatHidesText = type.getView(context)
                val text = type.getText(context)

                button.isClickable = false
                viewThatHidesText.visibility = View.VISIBLE
                text.setTextColor(Color.parseColor("#776666"))
            }
        }

        fun makeButtonClickable(type: Type, context: Context?){
            if (context != null) {
                val button = type.getButton(context)
                val viewThatHidesText = type.getView(context)
                val text = type.getText(context)

                button.isClickable = true
                viewThatHidesText.visibility = View.GONE
                text.setTextColor(Color.parseColor("#330000"))
            }
        }
    }
}