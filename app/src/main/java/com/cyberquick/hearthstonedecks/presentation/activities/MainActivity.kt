package com.cyberquick.hearthstonedecks.presentation.activities

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cyberquick.hearthstonedecks.BuildConfig
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.ActivityMainBinding
import com.cyberquick.hearthstonedecks.presentation.common.ToolbarTitleChanger
import com.cyberquick.hearthstonedecks.presentation.fragments.*
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
        initHomeButtonIcon()

        simpleNavigate(OnlinePageFragment())
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
                    showExitWindow()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        val versionText = getString(R.string.version_ph, BuildConfig.VERSION_NAME)
        binding.navigationDrawer.getHeaderView(0)
            .findViewById<TextView>(R.id.tv_app_version).text = versionText
    }

    private fun initHomeButtonIcon() {
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewCreated(
                    fm: FragmentManager, fragment: Fragment,
                    v: View, savedInstanceState: Bundle?
                ) {
                    super.onFragmentViewCreated(fm, fragment, v, savedInstanceState)
                    val newHomeButton = when (fragment) {
                        is DeckFragment, is AboutAppFragment -> HomeButton.Back
                        else -> HomeButton.Menu
                    }
                    if (newHomeButton == homeButton) return
                    homeButton = newHomeButton
                    val anim = when (newHomeButton) {
                        HomeButton.Back -> ValueAnimator.ofFloat(0f, 1f)
                        HomeButton.Menu -> ValueAnimator.ofFloat(1f, 0f)
                    }

                    anim.addUpdateListener { valueAnimator ->
                        val slideOffset = valueAnimator.animatedValue as Float
                        drawerToggle.onDrawerSlide(binding.drawerLayout, slideOffset)
                    }
                    anim.interpolator = DecelerateInterpolator()
                    anim.duration = 200     // 400
                    anim.start()
                }
            }, false
        )
    }

    private fun showExitWindow() {
        MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
            .setTitle(R.string.quit_app_question)
            .setPositiveButton(R.string.quit) { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack()
        else
            showExitWindow()
    }

    override fun setText(text: String) {
        binding.toolbar.title = text
    }
}