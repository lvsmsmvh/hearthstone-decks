package com.cyberquick.hearthstonedecks.presentation.activities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.ActivityMainBinding
import com.cyberquick.hearthstonedecks.presentation.common.Toolbar
import com.cyberquick.hearthstonedecks.presentation.fragments.*
import com.cyberquick.hearthstonedecks.utils.color
import com.cyberquick.hearthstonedecks.utils.drawable
import com.cyberquick.hearthstonedecks.utils.simpleNavigate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Toolbar {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.iconBack.setOnClickListener {
            onBackPressed()
        }

        initNavigationDrawer()
        simpleNavigate(OnlinePageFragment())
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack()
        else
            showExitWindow()
    }

    @SuppressLint("SetTextI18n")
    private fun initNavigationDrawer() {
        binding.toolbar.iconMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navigationDrawer)
        }

        binding.navigationDrawer.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.closeDrawer(binding.navigationDrawer)
            when (menuItem.itemId) {
                R.id.drawer_menu_item_all_decks -> {
                    simpleNavigate(OnlinePageFragment())
                }
                R.id.drawer_menu_item_my_decks -> {
                    simpleNavigate(FavoritePageFragment())
                }
                R.id.drawer_menu_item_about -> {
                    simpleNavigate(AboutAppFragment())
                }
                R.id.drawer_menu_item_logout -> {
                    showLogoutWindow()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        binding.navigationDrawer.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?, left: Int, top: Int, right: Int, bottom: Int,
                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
            ) {
                binding.navigationDrawer.removeOnLayoutChangeListener(this)
//                v?.findViewById<TextView>(R.id.tv_app_version)?.text =
//                    "Version: " + BuildConfig.VERSION_NAME
            }
        })
    }


    private fun showExitWindow() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Quit app?")
            .setPositiveButton("Quit") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(color(R.color.colorPrimary))
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(color(R.color.colorPrimary))
    }

    private fun showLogoutWindow() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Quit?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(color(R.color.colorPrimary))

        dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(color(R.color.colorPrimary))
    }

    /**
     * Toolbar configuration from Fragments
     */

    override fun updateLeftButtonState(left: Toolbar.LeftButtonState) {
        binding.toolbar.iconBack.isVisible = left is Toolbar.LeftButtonState.BackButton
        binding.toolbar.iconMenu.isVisible = left is Toolbar.LeftButtonState.MenuButton
    }

    override fun updateRightButtonState(right: Toolbar.RightButtonState) {
        binding.toolbar.iconAdditional.isVisible = right is Toolbar.RightButtonState.SaveButton

        when (right) {
            is Toolbar.RightButtonState.SaveButton -> {
                val iconId = when (right.isSaved) {
                    true -> R.drawable.ic_star_filled
                    false -> R.drawable.ic_star_not_filled
                }
                binding.toolbar.iconAdditional.setImageDrawable(drawable(iconId))
            }
            is Toolbar.RightButtonState.None -> {
            }
        }
    }

    override fun setRightButtonListener(onClickListener: View.OnClickListener) {
        binding.toolbar.iconAdditional.setOnClickListener(onClickListener)
    }

    override fun setText(text: String) {
        binding.toolbar.toolbarText.text = text
    }
}