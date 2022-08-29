package com.cyberquick.hearthstonedecks.presentation.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.cyberquick.hearthstonedecks.databinding.FragmentDeckBinding
import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.presentation.adapters.CardAdapter
import com.cyberquick.hearthstonedecks.presentation.adapters.DeckViewHolder
import com.cyberquick.hearthstonedecks.presentation.common.Toolbar
import com.cyberquick.hearthstonedecks.presentation.viewmodels.DeckViewModel
import com.cyberquick.hearthstonedecks.presentation.viewmodels.LoadingState
import com.cyberquick.hearthstonedecks.presentation.viewmodels.SavedState.*
import com.cyberquick.hearthstonedecks.presentation.viewmodels.asLoaded
import com.cyberquick.hearthstonedecks.utils.configureByDefault
import com.cyberquick.hearthstonedecks.utils.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeckFragment(private val deckPreview: DeckPreview) : BaseFragment() {

    private val viewModel: DeckViewModel by viewModels()
    private lateinit var binding: FragmentDeckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeckBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView() {
        toolbar.setText(deckPreview.title)
        toolbar.updateLeftButtonState(Toolbar.LeftButtonState.BackButton)

        val content = DeckViewHolder.Content(
            root = binding.deckPreview.root,
            title = binding.deckPreview.deckTitle,
            dust = binding.deckPreview.deckDust,
            deckClassImg = binding.deckPreview.deckClassImg,
            deckFormat = binding.deckPreview.deckFormat,
            deckFormatImg = binding.deckPreview.deckFormatImg,
            deckTimeCreated = binding.deckPreview.deckTimeCreated,
            views = binding.deckPreview.deckViews,
        )

        DeckViewHolder(content).bind(
            deckPreview,
            onLoadedListener = {
                setEnterSharedElementCallback(
                    object : SharedElementCallback() {
                        override fun onMapSharedElements(
                            names: List<String?>, sharedElements: MutableMap<String?, View?>
                        ) {
                            sharedElements[names[0]] = binding.deckPreview.root
                        }
                    })
            },
        )

        requireView().doOnPreDraw { startPostponedEnterTransition() }

        toolbar.setRightButtonListener {
            viewModel.stateDeck.value.asLoaded()?.result?.let { deck ->
                viewModel.stateCards.value.asLoaded()?.result?.let { cards ->
                    viewModel.clickedOnSaveButton(deck, cards)
                    return@setRightButtonListener
                }
            }

            toast("A deck is not loaded yet.")
        }
    }

    private fun initData() {
        viewModel.stateDeckSaved.observe(viewLifecycleOwner) { state ->
            toolbar.updateRightButtonState(Toolbar.RightButtonState.SaveButton(state is Saved))
        }

        viewModel.stateDeck.observe(viewLifecycleOwner) { state ->
            binding.layoutProgressBar.layoutProgressBar.isVisible = state is LoadingState.Loading
            binding.layoutFailed.layoutFailed.isVisible = state is LoadingState.Failed
            binding.btnDescription.cardViewBtnDescription.isVisible = state is LoadingState.Loaded

            if (state is LoadingState.Failed) {
                binding.layoutFailed.tvErrorLoadingDataSmall.text = state.message
            }

            if (state is LoadingState.Loaded) {
                val deck = state.result
                viewModel.loadCards(deck)
                binding.btnDescription.root.isVisible = deck.description.isNotBlank()
                binding.btnDescription.detDescription.configureByDefault(
                    text = deck.description,
                    layoutContainer = binding.btnDescription.detDescriptionLayout,
                    arrow = binding.btnDescription.detImgArrowUpDown,
                )
                binding.deckHolder.btnCopyDeck.setOnClickListener {
                    copyToClipboard(deck)
                }
            }
        }

        viewModel.stateCards.observe(viewLifecycleOwner) { state ->
            binding.layoutProgressBar.layoutProgressBar.isVisible = state is LoadingState.Loading
            binding.layoutFailed.layoutFailed.isVisible = state is LoadingState.Failed
            binding.deckHolder.llDeckHolder.isVisible = state is LoadingState.Loaded

            if (state is LoadingState.Failed) {
                binding.layoutFailed.tvErrorLoadingDataSmall.text = state.message
            }

            if (state is LoadingState.Loaded) {
                val cards = state.result

                binding.deckHolder.recycleViewCards.apply {
                    layoutManager = GridLayoutManager(
                        context, CardAdapter.TOTAL_ITEMS_HORIZONTAL,
                        LinearLayoutManager.VERTICAL, false
                    )
                    adapter = CardAdapter().apply { set(cards) }
                    isNestedScrollingEnabled = false
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->
            toast(message)
        }

        viewModel.updateIsDeckSaved(deckPreview)
        viewModel.loadDeck(deckPreview)
    }


    private fun copyToClipboard(deck: Deck) {
        (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText("copy", deck.code))
        toast("Copied to clipboard!")
    }
}