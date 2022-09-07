package com.cyberquick.hearthstonedecks.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.core.app.SharedElementCallback
import androidx.core.view.*
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.FragmentPageBinding
import com.cyberquick.hearthstonedecks.domain.exceptions.NoSavedDecksFoundException
import com.cyberquick.hearthstonedecks.presentation.adapters.DeckAdapter
import com.cyberquick.hearthstonedecks.presentation.viewmodels.*
import com.cyberquick.hearthstonedecks.utils.color
import com.cyberquick.hearthstonedecks.utils.simpleNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnlinePageFragment : PageFragment() {
    override val viewModel: OnlinePageViewModel by viewModels()
}

@AndroidEntryPoint
class FavoritePageFragment : PageFragment() {
    override val viewModel: FavoritePageViewModel by viewModels()
}

abstract class PageFragment : BaseFragment(), MenuProvider {

    private lateinit var binding: FragmentPageBinding
    private lateinit var deckAdapter: DeckAdapter
    private var menu: Menu? = null

    private var deckIdClicked: Int? = null
    private var clickedOnDeck = false
    private var wasPreviouslyLoaded = false

    abstract val viewModel: PageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageBinding.inflate(layoutInflater)
        if (clickedOnDeck) {
            postponeEnterTransition()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView() {
        requireActivity().addMenuProvider(this, viewLifecycleOwner)

        deckAdapter = DeckAdapter(
            onAnimateItemReady = {
                (exitTransition as? Transition)?.excludeTarget(
                    it.content.root, true
                )
                setExitSharedElementCallback(object : SharedElementCallback() {
                    override fun onMapSharedElements(
                        names: List<String>, sharedElements: MutableMap<String, View>
                    ) {
                        exitTransition = null
                        sharedElements[names[0]] = it.content.root
                    }
                })
            },
            onClickListener = { data ->
                clickedOnDeck = true
                deckIdClicked = data.deckPreview.id
                val targetView = data.content.root

                val transition = TransitionInflater.from(requireContext())
                    .inflateTransition(R.transition.items_exit_transition)
                transition.addTarget(R.id.card_view)
                transition.excludeTarget(targetView, true)
                exitTransition = transition
                setExitSharedElementCallback(object : SharedElementCallback() {
                    override fun onMapSharedElements(
                        names: List<String>, sharedElements: MutableMap<String, View>
                    ) {
                        sharedElements[names[0]] = targetView
                    }
                })

                val fragment = DeckFragment(data.deckPreview)
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .addSharedElement(targetView, targetView.transitionName)
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(fragment.javaClass.name)
                    .commit()
            },
        )

        binding.recycleViewDecks.layoutManager = LinearLayoutManager(context)
        binding.recycleViewDecks.adapter = deckAdapter
        binding.layoutFailed.btnReloadData.setOnClickListener {
            viewModel.updateCurrentPage(evenIfLoaded = true)
        }
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
                    updateLayout(state)
                    wasPreviouslyLoaded = true
                    deckAdapter.deckPreviewIdToAnimate = deckIdClicked
                    deckAdapter.set(state.result.deckPreviews)
                }
            }
        }

        doOnExitTransitionEnd {
            viewModel.updateCurrentPage(evenIfLoaded = this is FavoritePageFragment)
        }

        if (clickedOnDeck) {
            clickedOnDeck = false
            requireView().doOnPreDraw {
                startPostponedEnterTransition()
            }
        }
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
                    getString(R.string.no_saved_decks_found_title)
                binding.layoutFailed.tvErrorLoadingDataSmall.text =
                    getString(R.string.no_saved_decks_found_desc)
                binding.layoutFailed.btnReloadData.isVisible = false
            }
            else -> {
                binding.layoutFailed.tvErrorLoadingData.text =
                    getString(R.string.error_loading_data)
                binding.layoutFailed.tvErrorLoadingDataSmall.text = exception.message
                binding.layoutFailed.btnReloadData.isVisible = true
                if (this is OnlinePageFragment) {
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
        this.menu = menu
        updateMenuButtons(viewModel.allowNavigation.value!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        menu = null
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_button_previous -> viewModel.loadPreviousPage()
            R.id.menu_button_next -> viewModel.loadNextPage()
        }
        return true
    }

    private fun updateMenuButtons(allowNavigation: PageViewModel.AllowNavigation) =
        menu?.let { menu ->
            menu.previousButton().update(allowNavigation.previous)
            menu.nextButton().update(allowNavigation.next)
            onPrepareMenu(menu)
        }

    private fun Menu.previousButton() = findItem(R.id.menu_button_previous)
    private fun Menu.nextButton() = findItem(R.id.menu_button_next)
    private fun MenuItem.update(isActive: Boolean) {
        icon?.setTint(color(if (isActive) R.color.palette_100 else R.color.palette_700))
        isEnabled = isActive
    }
}