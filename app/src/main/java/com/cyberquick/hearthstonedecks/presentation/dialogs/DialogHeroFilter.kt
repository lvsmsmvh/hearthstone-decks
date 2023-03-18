package com.cyberquick.hearthstonedecks.presentation.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.DialogHeroFilterBinding
import com.cyberquick.hearthstonedecks.domain.entities.Hero
import com.cyberquick.hearthstonedecks.presentation.adapters.HeroAdapter

class DialogHeroFilter(
    context: Context,
    private val previouslySelected: Set<Hero>,
    private val onNewSelected: (Set<Hero>) -> Unit,
) : Dialog(context, R.style.DialogHeroFilterStyle) {

    private lateinit var binding: DialogHeroFilterBinding
    private var isAllItemsSelected = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogHeroFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = HeroAdapter(
            previouslySelected = previouslySelected,
            isAllSelectedListener = { isAllSelected ->
                Log.i("tag_hero", "isAllSelectedListener, check = $isAllSelected")
                binding.checkboxSelectAll.isChecked = isAllSelected
                isAllItemsSelected = isAllSelected
            }
        )

        binding.recycleViewHero.adapter = adapter
        binding.recycleViewHero.layoutManager = LinearLayoutManager(
            context, RecyclerView.VERTICAL, false
        )

        isAllItemsSelected = previouslySelected.size == Hero.values().size
        binding.checkboxSelectAll.isChecked = isAllItemsSelected

        binding.checkboxSelectAll.setOnClickListener {
            val isCheckedOld = isAllItemsSelected
            val isCheckedNew = isCheckedOld.not()
            Log.i("tag_hero", "Select all click, old = $isCheckedOld, new = $isCheckedNew")
            binding.checkboxSelectAll.isChecked = isCheckedNew
            when (isCheckedNew) {
                true -> adapter.selectAll()
                false -> adapter.unselectAll()
            }
            isAllItemsSelected = isCheckedNew
        }
        binding.checkboxSelectAll.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.i("tag_hero", "OnCheckedChangeListener, new = $isChecked")
        }

        binding.btnNo.setOnClickListener {
            dismiss()
        }

        binding.btnYes.setOnClickListener {
            onNewSelected(adapter.getSelected())
            dismiss()
        }
    }
}