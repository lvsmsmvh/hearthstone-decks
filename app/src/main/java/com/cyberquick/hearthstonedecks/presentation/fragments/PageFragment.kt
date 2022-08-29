package com.cyberquick.hearthstonedecks.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.FragmentPageBinding
import com.cyberquick.hearthstonedecks.presentation.adapters.DeckAdapter
import com.cyberquick.hearthstonedecks.presentation.common.Toolbar
import com.cyberquick.hearthstonedecks.presentation.viewmodels.*
import com.cyberquick.hearthstonedecks.utils.setActive
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates


@AndroidEntryPoint
class OnlinePageFragment : PageFragment() {
    override val viewModel: OnlinePageViewModel by viewModels()
}

@AndroidEntryPoint
class FavoritePageFragment : PageFragment() {
    override val viewModel: FavoritePageViewModel by viewModels()
}

abstract class PageFragment : BaseFragment() {

    private lateinit var binding: FragmentPageBinding
    private lateinit var deckAdapter: DeckAdapter
    private var totalPages by Delegates.notNull<Int>()
    private var currentPageNumber = 1
    private var deckPreviewIdClicked: Int? = null

    abstract val viewModel: PageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageBinding.inflate(layoutInflater)
        postponeEnterTransition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView() {
        toolbar.updateLeftButtonState(Toolbar.LeftButtonState.MenuButton)
        toolbar.updateRightButtonState(Toolbar.RightButtonState.None)

        deckAdapter = DeckAdapter()
        binding.recycleViewDecks.layoutManager = LinearLayoutManager(context)
        binding.recycleViewDecks.adapter = deckAdapter
        binding.layoutFailed.btnReloadData.setOnClickListener {
            loadPage(currentPageNumber)
        }
        binding.btnPrevious.setOnClickListener {
            loadPage(currentPageNumber - 1)
        }
        binding.btnNext.setOnClickListener {
            loadPage(currentPageNumber + 1)
        }
    }

    private fun initData() {
        viewModel.amountOfPages.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadingState.Idle -> {
                }
                is LoadingState.Loading -> {
                    updateLayout(state)
                    toolbar.setText("Loading...")
                }
                is LoadingState.Failed -> {
                    updateLayout(state)
                }
                is LoadingState.Loaded -> {
                    totalPages = state.result
                    loadPage(currentPageNumber)
                }
            }
        }

        viewModel.page.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadingState.Idle -> {
                }
                is LoadingState.Loading -> {
                    updateLayout(state)
                }
                is LoadingState.Failed -> {
                    updateLayout(state)
                }
                is LoadingState.Loaded -> {
                    updateLayout(state)

                    deckAdapter.set(
                        list = state.result.deckPreviews,
                        deckPreviewIdToAnimate = deckPreviewIdClicked,
                        onAnimateItemReady = {
                            setExitSharedElementCallback(object : SharedElementCallback() {
                                override fun onMapSharedElements(
                                    names: List<String>, sharedElements: MutableMap<String, View>
                                ) {
                                    sharedElements[names[0]] = it.content.root
                                }
                            })
                        },
                        onClickListener = { data ->
                            deckPreviewIdClicked = data.deckPreview.id

                            val targetView = data.content.root
                            setExitSharedElementCallback(object : SharedElementCallback() {
                                override fun onMapSharedElements(
                                    names: List<String>, sharedElements: MutableMap<String, View>
                                ) {
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

                    requireView().doOnPreDraw { startPostponedEnterTransition() }
                }
            }
        }

        viewModel.loadAmountOfPages()
    }

    private fun loadPage(pageIndex: Int) {
        currentPageNumber = pageIndex
        viewModel.loadPage(pageIndex)
        toolbar.setText("Page: $currentPageNumber/$totalPages")
    }

    private fun updateLayout(loadingState: LoadingState<Any>) {
        binding.layoutLoading.layoutProgressBar.isVisible = loadingState.isLoading()
        binding.layoutFailed.layoutFailed.isVisible = loadingState.isFailed()
        binding.layoutLoaded.isVisible = loadingState.isLoaded()

        binding.btnPrevious.setActive(loadingState.isLoaded() && currentPageNumber > 1)
        binding.btnNext.setActive(loadingState.isLoaded() && currentPageNumber < totalPages)

        if (loadingState is LoadingState.Failed) {
            binding.layoutFailed.tvErrorLoadingDataSmall.text = loadingState.message
        }
    }
}