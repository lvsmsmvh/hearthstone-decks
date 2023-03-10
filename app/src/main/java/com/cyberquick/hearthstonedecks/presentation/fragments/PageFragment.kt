package com.cyberquick.hearthstonedecks.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.*
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.FragmentPageBinding
import com.cyberquick.hearthstonedecks.domain.common.deckPreviewToJson
import com.cyberquick.hearthstonedecks.domain.exceptions.NoSavedDecksFoundException
import com.cyberquick.hearthstonedecks.presentation.adapters.DeckAdapter
import com.cyberquick.hearthstonedecks.presentation.fragments.base.BaseFragment
import com.cyberquick.hearthstonedecks.presentation.viewmodels.*
import com.cyberquick.hearthstonedecks.utils.color
import com.cyberquick.hearthstonedecks.utils.simpleNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnlineStandardPageFragment : PageFragment() {
    override val viewModel: OnlineStandardPageViewModel by viewModels()
}

@AndroidEntryPoint
class OnlineWildPageFragment : PageFragment() {
    override val viewModel: OnlineWildPageViewModel by viewModels()
}

@AndroidEntryPoint
class FavoritePageFragment : PageFragment() {
    override val viewModel: FavoritePageViewModel by viewModels()
}

abstract class PageFragment : BaseFragment(), MenuProvider {

    private var _binding: FragmentPageBinding? = null
    private val binding get() = _binding!!

    private var _deckAdapter: DeckAdapter? = null
    private val deckAdapter get() = _deckAdapter!!

    private var _menu: Menu? = null

    private var wasPreviouslyLoaded = false

    protected abstract val viewModel: PageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView() {
        requireActivity().addMenuProvider(this, viewLifecycleOwner)

        binding.layoutFailed.btnReloadData.setOnClickListener {
            viewModel.updateCurrentPage(evenIfLoaded = true)
        }

        binding.recycleViewDecks.layoutManager = LinearLayoutManager(context)
        binding.recycleViewDecks.adapter = DeckAdapter(
            onClickListener = { data ->
                val fragment = DeckFragment()
                fragment.arguments = Bundle().apply {
                    putString(DeckFragment.KEY_DECK_PREVIEW, deckPreviewToJson(data.deckPreview))
                }
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right
                    )
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(fragment.javaClass.name)
                    .commit()
            },
        ).also { _deckAdapter = it }
    }

    private fun initData() {
        viewModel.position.observe(viewLifecycleOwner) {
            toolbarTitleChanger.setText("${it.current}/${it.total ?: "..."}")
        }

        viewModel.allowNavigation.observe(viewLifecycleOwner) {
            updateMenuButtons(it)
        }

        viewModel.pageLoading.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadingState.Loading -> {
                    updateLayout(state)
                }

                is LoadingState.Failed -> {
                    updateLayout(state)
                }

                is LoadingState.Loaded -> {
                    Log.i("tag_page", "Page ${state.result.number} loaded")
                    val ratings = state.result.deckPreviews.map { it.rating }
                    Log.i("tag_page", "Ratings : ${ratings}")
                    deckAdapter.set(state.result.deckPreviews)
                    updateLayout(state)
                    wasPreviouslyLoaded = true
                }
            }
        }

        // do on resume maybe
        viewModel.updateCurrentPage(evenIfLoaded = this is FavoritePageFragment)
    }

    private fun updateLayout(loadingState: LoadingState<Any>) {
        val hideLoading = this is FavoritePageFragment && wasPreviouslyLoaded
        binding.layoutLoading.layoutProgressBar.isVisible =
            loadingState.isLoading() && !hideLoading
        binding.layoutFailed.layoutFailed.isVisible = loadingState.isFailed()
        binding.layoutLoaded.isVisible = loadingState.isLoaded()
                || (loadingState.isLoading() && hideLoading)

        when (val exception = (loadingState as? LoadingState.Failed)?.exception ?: return) {
            is NoSavedDecksFoundException -> {
                binding.layoutFailed.tvErrorLoadingData.text =
                    getString(R.string.error_no_saved_decks_found_title)
                binding.layoutFailed.tvErrorLoadingDataSmall.text =
                    getString(R.string.error_no_saved_decks_found_desc)
                binding.layoutFailed.btnReloadData.isVisible = false
            }

            else -> {
                binding.layoutFailed.tvErrorLoadingData.text =
                    getString(R.string.error_loading_decks)
                binding.layoutFailed.tvErrorLoadingDataSmall.text = exception.message
                binding.layoutFailed.btnReloadData.isVisible = true
                if (this !is FavoritePageFragment) {
                    binding.layoutFailed.btnOpenFavorite.isVisible = true
                    binding.layoutFailed.btnOpenFavorite.setOnClickListener {
                        requireActivity().simpleNavigate(FavoritePageFragment())
                    }
                }
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_fragment_deck, menu)
        this._menu = menu
        updateMenuButtons(viewModel.allowNavigation.value!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _deckAdapter = null
        _binding = null
        _menu = null
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_button_previous -> {
                deckAdapter.clear()
                viewModel.loadPreviousPage()
            }
            R.id.menu_button_next -> {
                deckAdapter.clear()
                viewModel.loadNextPage()
            }
        }
        return true
    }

    private fun updateMenuButtons(allowNavigation: PageViewModel.AllowNavigation) {
        _menu?.let { menu ->
            menu.previousButton().update(allowNavigation.previous)
            menu.nextButton().update(allowNavigation.next)
            onPrepareMenu(menu)
        }
    }

    private fun Menu.previousButton() = findItem(R.id.menu_button_previous)
    private fun Menu.nextButton() = findItem(R.id.menu_button_next)
    private fun MenuItem.update(isActive: Boolean) {
        icon?.setTint(color(if (isActive) R.color.palette_100 else R.color.palette_700))
        isEnabled = isActive
    }
}