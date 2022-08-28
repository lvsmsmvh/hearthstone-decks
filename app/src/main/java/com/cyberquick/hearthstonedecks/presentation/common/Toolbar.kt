package com.cyberquick.hearthstonedecks.presentation.common

import android.view.View

interface Toolbar {
    fun setText(text: String)
    fun updateLeftButtonState(left: LeftButtonState)
    fun updateRightButtonState(right: RightButtonState)
    fun setRightButtonListener(onClickListener: View.OnClickListener)

    sealed class LeftButtonState {
        object MenuButton : LeftButtonState()
        object BackButton : LeftButtonState()
    }

    sealed class RightButtonState {
        object None : RightButtonState()
        data class SaveButton(val isSaved: Boolean) : RightButtonState()
    }
}