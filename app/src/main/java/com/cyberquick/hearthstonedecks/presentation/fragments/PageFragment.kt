package com.cyberquick.hearthstonedecks.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.FragmentPageBinding
import com.cyberquick.hearthstonedecks.domain.common.NoSavedDecksFoundException
import com.cyberquick.hearthstonedecks.presentation.adapters.DeckAdapter
import com.cyberquick.hearthstonedecks.presentation.viewmodels.*
import com.cyberquick.hearthstonedecks.utils.setActive
import com.cyberquick.hearthstonedecks.utils.simpleNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class OnlinePageFragment : PageFragment() {
    override val viewModel: OnlinePageViewModel by viewModels()
}

@AndroidEntryPoint
class FavoritePageFragment : PageFragment() {
    override val viewModel: FavoritePageViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        viewModel.updateCurrentPage()
    }
}

abstract class PageFragment : BaseFragment() {

    private lateinit var binding: FragmentPageBinding
    private lateinit var deckAdapter: DeckAdapter
    private var totalPages by Delegates.notNull<Int>()
    private var currentPageNumber = 1
    private var deckPreviewIdClicked: Int? = null
    private var clickedOnDeck = false

    abstract val viewModel: PageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageBinding.inflate(layoutInflater)
        if (clickedOnDeck) {
            postponeEnterTransition()
            sharedElementEnterTransition = TransitionInflater.from(context)
                .inflateTransition(android.R.transition.move)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView() {
        deckAdapter = DeckAdapter()
        binding.recycleViewDecks.layoutManager = LinearLayoutManager(context)
        binding.recycleViewDecks.adapter = deckAdapter
        binding.layoutFailed.btnReloadData.setOnClickListener {
            loadPage(currentPageNumber)
        }

        binding.btnPrevious.setOnClickListener {
            loadPage(currentPageNumber - 1)
            updateButtons()
        }
        binding.btnNext.setOnClickListener {
            loadPage(currentPageNumber + 1)
            updateButtons()
        }
    }

    private fun initData() {
        viewModel.amountOfPages.observe(viewLifecycleOwner) { state ->
            Log.i("tag_state", "STATE amountOfPages ${state.javaClass.simpleName}")
            when (state) {
                is LoadingState.Loading -> {
                    updateLayout(state)
                    toolbarTitleChanger.setText("Loading...")
                }
                is LoadingState.Failed -> {
                    updateLayout(state)
                }
                is LoadingState.Loaded -> {
                    totalPages = state.result
                    loadPage(currentPageNumber)
                    updateButtons()
                }
            }
        }

        viewModel.page.observe(viewLifecycleOwner) { state ->
            Log.i("tag_state", "STATE page ${state.javaClass.simpleName}")
            when (state) {
                is LoadingState.Loading -> {
                    Log.i("tag_anim", "Page state : Loading")
                    updateLayout(state)
                }
                is LoadingState.Failed -> {
                    Log.i("tag_anim", "Page state : Failed")
                    updateLayout(state)
                }
                is LoadingState.Loaded -> {
                    Log.i("tag_anim", "Page state : Loaded")
                    updateLayout(state)
                    deckAdapter.set(
                        list = state.result.deckPreviews,
                        deckPreviewIdToAnimate = deckPreviewIdClicked,
                        onAnimateItemReady = {
                            setExitSharedElementCallback(object : SharedElementCallback() {
                                override fun onMapSharedElements(
                                    names: List<String>, sharedElements: MutableMap<String, View>
                                ) {
                                    Log.i(
                                        "tag_anim",
                                        "setExitSharedElementCallback() : prepare for RETURN anim"
                                    )
                                    exitTransition = null
                                    sharedElements[names[0]] = it.content.root
                                }
                            })
                            if (clickedOnDeck) {
                                Log.i(
                                    "tag_anim",
                                    "startPostponedEnterTransition"
                                )
                                clickedOnDeck = false
                                requireView().doOnPreDraw {
                                    startPostponedEnterTransition()
                                }
                            }
                        },
                        onClickListener = { data ->
                            clickedOnDeck = true
                            deckPreviewIdClicked = data.deckPreview.id
                            exitTransition = TransitionInflater.from(requireContext())
                                .inflateTransition(R.transition.items_exit_transition)

                            val targetView = data.content.root
                            setExitSharedElementCallback(object : SharedElementCallback() {
                                override fun onMapSharedElements(
                                    names: List<String>, sharedElements: MutableMap<String, View>
                                ) {
                                    Log.i(
                                        "tag_anim",
                                        "setExitSharedElementCallback() : prepare for ENTER anim"
                                    )
                                    sharedElements[names[0]] = data.content.root
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
                }
            }
        }

        viewModel.loadAmountOfPages()
    }

    private fun loadPage(pageIndex: Int) {
        currentPageNumber = pageIndex
        viewModel.loadPage(pageIndex)
        toolbarTitleChanger.setText("Page: $currentPageNumber/$totalPages")
    }

    private fun updateLayout(loadingState: LoadingState<Any>) {
        binding.layoutLoading.layoutProgressBar.isVisible = loadingState.isLoading()
        binding.layoutFailed.layoutFailed.isVisible = loadingState.isFailed()
        binding.layoutLoaded.isVisible = loadingState.isLoaded()

        when (val exception = (loadingState as? LoadingState.Failed)?.exception) {
            null -> {
                Log.i("tag_state", "Failed: exception is null")
            }
            is NoSavedDecksFoundException -> {
                Log.i("tag_state", "Failed: exception is NoSavedDecksFoundException")
                binding.layoutFailed.tvErrorLoadingData.text =
                    getString(R.string.no_saved_decks_found_title)
                binding.layoutFailed.tvErrorLoadingDataSmall.text =
                    getString(R.string.no_saved_decks_found_desc)
                binding.layoutFailed.btnReloadData.isVisible = false
            }
            else -> {
                Log.i("tag_state", "Failed: exception is else")
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

    private fun updateButtons() {
        binding.btnPrevious.setActive(currentPageNumber > 1)
        binding.btnNext.setActive(currentPageNumber < totalPages)
    }
}