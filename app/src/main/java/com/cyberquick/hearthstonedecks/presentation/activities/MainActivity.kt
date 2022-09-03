package com.cyberquick.hearthstonedecks.presentation.activities

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.ActivityMainBinding
import com.cyberquick.hearthstonedecks.presentation.common.ToolbarTitleChanger
import com.cyberquick.hearthstonedecks.presentation.fragments.*
import com.cyberquick.hearthstonedecks.utils.color
import com.cyberquick.hearthstonedecks.utils.simpleNavigate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ToolbarTitleChanger {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle

    private enum class HomeButton { Menu, Back; }

    private var homeButton = HomeButton.Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initNavigationDrawer()

        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewCreated(
                    fm: FragmentManager, fragment: Fragment,
                    v: View, savedInstanceState: Bundle?
                ) {
                    super.onFragmentViewCreated(fm, fragment, v, savedInstanceState)
                    Log.i("tag_nav", "Fragment created ${fragment.javaClass.simpleName}")
                    val newHomeButton = when (fragment) {
                        is DeckFragment, is AboutAppFragment -> HomeButton.Back
                        else -> HomeButton.Menu
                    }
                    if (newHomeButton != homeButton) {
                        homeButton = newHomeButton
                        updateHomeButtonState(newHomeButton)
                    }
                }
            }, false
        )
        simpleNavigate(OnlinePageFragment())
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack()
        else
            finish()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        (supportActionBar as ActionBar).apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_menu_go_back,
            R.string.open_menu_go_back,
        )
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        binding.toolbar.setNavigationOnClickListener {
            when (homeButton) {
                HomeButton.Menu -> binding.drawerLayout.openDrawer(binding.navigationDrawer)
                HomeButton.Back -> onBackPressed()
            }
        }
    }

    private fun updateHomeButtonState(state: HomeButton) {
        val anim =
            if (state == HomeButton.Back) ValueAnimator.ofFloat(0f, 1f)
            else ValueAnimator.ofFloat(1f, 0f)

        anim.addUpdateListener { valueAnimator ->
            val slideOffset = valueAnimator.animatedValue as Float
            drawerToggle.onDrawerSlide(binding.drawerLayout, slideOffset)
        }
        anim.interpolator = DecelerateInterpolator()
        anim.duration = 200     // 400
        anim.start()
    }

    @SuppressLint("SetTextI18n")
    private fun initNavigationDrawer() {
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
                R.id.drawer_menu_item_exit -> {
                    finish()
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

    override fun setText(text: String) {
        binding.toolbar.title = text
    }
}