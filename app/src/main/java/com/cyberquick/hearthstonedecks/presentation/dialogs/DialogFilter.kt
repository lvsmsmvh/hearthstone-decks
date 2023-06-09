package com.cyberquick.hearthstonedecks.presentation.dialogs

import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.DialogHeroFilterBinding
import com.cyberquick.hearthstonedecks.domain.entities.GetPageFilter
import com.cyberquick.hearthstonedecks.domain.entities.Hero
import com.cyberquick.hearthstonedecks.presentation.adapters.HeroAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


object DialogFilter {
    fun show(
        context: Context,
        previousFilter: GetPageFilter,
        onNewSelected: (GetPageFilter) -> Unit,
    ) {
        var isAllItemsSelected: Boolean

        val dialog = BottomSheetDialog(context, R.style.DialogHeroFilterStyle)
        val binding = DialogHeroFilterBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(binding.root)

        dialog.setOnDismissListener {
        }

        val adapter = HeroAdapter(
            previouslySelected = previousFilter.heroes,
            isAllSelectedListener = { isAllSelected ->
                binding.checkboxSelectAll.isChecked = isAllSelected
                isAllItemsSelected = isAllSelected
            }
        )

        binding.recycleViewHero.adapter = adapter
        binding.recycleViewHero.layoutManager = GridLayoutManager(
            context, 2, RecyclerView.VERTICAL, false
        )

        isAllItemsSelected = previousFilter.heroes.size == Hero.values().size
        binding.checkboxSelectAll.isChecked = isAllItemsSelected

        val selectAllListener = OnClickListener {
            val isCheckedOld = isAllItemsSelected
            val isCheckedNew = isCheckedOld.not()
            binding.checkboxSelectAll.isChecked = isCheckedNew
            when (isCheckedNew) {
                true -> adapter.selectAll()
                false -> adapter.unselectAll()
            }
            isAllItemsSelected = isCheckedNew
        }

        binding.checkboxSelectAll.setOnClickListener(selectAllListener)
        binding.textSelectAll.setOnClickListener(selectAllListener)

        binding.btnNo.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnYes.setOnClickListener {
            val text = binding.etPrompt.text.toString()
            val heroes = adapter.getSelected()
            val filter = GetPageFilter(text, heroes)
            onNewSelected(filter)
            dialog.dismiss()
        }


        // This listener's onShow is fired when the dialog is shown
        // This listener's onShow is fired when the dialog is shown
        dialog.setOnShowListener { _ -> // In a previous life I used this method to get handles to the positive and negative buttons
            // of a dialog in order to change their Typeface. Good ol' days.

            // This is gotten directly from the source of BottomSheetDialog
            // in the wrapInBottomSheet() method
            val bottomSheet = dialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            ) as? FrameLayout ?: return@setOnShowListener

            // Right here!
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
        dialog.show()
    }
}