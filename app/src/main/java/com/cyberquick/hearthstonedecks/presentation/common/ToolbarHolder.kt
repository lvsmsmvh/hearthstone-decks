package com.cyberquick.hearthstonedecks.presentation.common


interface ToolbarHolder {

    fun showToolbar()
    fun hideToolbar()

    fun showHomeButtonAsMenu()
    fun showHomeButtonAsBack()

    fun setText(text: String)
}