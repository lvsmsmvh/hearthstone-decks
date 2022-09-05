package com.cyberquick.hearthstonedecks.presentation.fragments

import android.animation.LayoutTransition
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.FragmentDeckBinding
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.presentation.adapters.CardSmallAdapter
import com.cyberquick.hearthstonedecks.presentation.adapters.DeckViewHolder
import com.cyberquick.hearthstonedecks.presentation.viewmodels.DeckViewModel
import com.cyberquick.hearthstonedecks.presentation.viewmodels.LoadingState
import com.cyberquick.hearthstonedecks.presentation.viewmodels.SavedState.Saved
import com.cyberquick.hearthstonedecks.utils.drawable
import com.cyberquick.hearthstonedecks.utils.expand
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
        toolbarTitleChanger.setText(deckPreview.title)

        val content = DeckViewHolder.Content(
            root = binding.deckPreview.root,
            title = binding.deckPreview.deckTitle,
            dust = binding.deckPreview.deckDust,
            deckClassImg = binding.deckPreview.deckClassImg,
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
                            val target = binding.deckPreview.root
                            sharedElements[names[0]] = target
                        }
                    })
            },
        )

        binding.deckPreview.cardView.setOnClickListener {
            requireActivity().onBackPressed()
        }

        requireView().doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun initData() {
        binding.layoutProgressBar.layoutProgressBar.isVisible = true

        viewModel.stateDeck.observe(viewLifecycleOwner) { state ->
            if (state is LoadingState.Loading) {
                binding.layoutProgressBar.layoutProgressBar.isVisible = true
            }

            if (state is LoadingState.Failed) {
                binding.layoutProgressBar.layoutProgressBar.isVisible = false
                binding.layoutFailed.layoutFailed.isVisible = true
                binding.layoutFailed.tvErrorLoadingDataSmall.text = state.exception.message.toString()
                binding.layoutFailed.btnReloadData.setOnClickListener {
                    binding.layoutFailed.layoutFailed.isVisible = false
                    viewModel.loadDeck(deckPreview)
                }
            }

            if (state is LoadingState.Loaded) {
                val deck = state.result

                binding.btnCopyDeck.setOnClickListener {
                    binding.btnCopyDeckIcon.setImageDrawable(drawable(R.drawable.ic_copy_filled))
                    copyToClipboard(deck.code)
                }
                binding.btnShare.setOnClickListener {
                    shareLink(deck.deckPreview.deckUrl)
                }

                val cards = state.result.cards

                binding.recycleViewCards.apply {
                    layoutManager = GridLayoutManager(
                        context, CardSmallAdapter.TOTAL_ITEMS_HORIZONTAL,
                        LinearLayoutManager.VERTICAL, false
                    )
                    adapter = CardSmallAdapter().apply { set(cards) }
                    isNestedScrollingEnabled = false
                }

                binding.btnFavorite.layoutTransition
                    .enableTransitionType(LayoutTransition.CHANGING)

                binding.btnFavorite.setOnClickListener {
                    viewModel.clickedOnSaveButton(deck, cards)
                }

                doOnEnterTransitionEnd {
                    binding.layoutProgressBar.layoutProgressBar.isVisible = false
                    binding.deckHolder.expand()
                }
            }
        }

        viewModel.stateDeckSaved.observe(viewLifecycleOwner) { state ->
            val isSaved = state is Saved

            val newIconRes = when (isSaved) {
                true -> R.drawable.ic_star_filled
                false -> R.drawable.ic_star_not_filled
            }
            val newTextRes = when (isSaved) {
                true -> R.string.saved
                false -> R.string.save
            }

            binding.btnFavoriteIcon.setImageDrawable(drawable(newIconRes))
            binding.btnFavoriteText.setText(newTextRes)
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->
            toast(message)
        }

        viewModel.updateIsDeckSaved(deckPreview)
        viewModel.loadDeck(deckPreview)
    }

    private fun copyToClipboard(text: String) {
        (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText("copy", text))
        toast(getString(R.string.copied_to_clipboard))
    }

    private fun shareLink(url: String) {
        ShareCompat.IntentBuilder(requireContext())
            .setType("text/plain")
            .setChooserTitle(getString(R.string.share_url))
            .setText(url)
            .startChooser()
    }
}