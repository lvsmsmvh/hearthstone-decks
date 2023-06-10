package com.cyberquick.hearthstonedecks.presentation.dialogs

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnFocusChangeListener
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.doOnAttach
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.DialogFilterBinding
import com.cyberquick.hearthstonedecks.domain.entities.GetPageFilter
import com.cyberquick.hearthstonedecks.domain.entities.Hero
import com.cyberquick.hearthstonedecks.presentation.adapters.HeroAdapter
import com.cyberquick.hearthstonedecks.utils.setupFullHeight
import com.cyberquick.hearthstonedecks.utils.showKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class DialogFilter(context: Context) : BottomSheetDialog(context, R.style.DialogHeroFilterStyle) {

    /**
     * Hide keyboard on outside-of-edit-text touch.
     */

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Log.i("tag_edit", "dispatchTouchEvent ACTION ${event.action}")

        if (event.action == MotionEvent.ACTION_DOWN) {

            val v: View? = currentFocus
            if (v is EditText) {
                Log.i("tag_edit", "dispatchTouchEvent v is EditText")

                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    Log.i("tag_edit", "dispatchTouchEvent - hide")

                    v.clearFocus()
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm?.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    companion object {
        fun show(
            context: Context,
            previousFilter: GetPageFilter,
            onNewSelected: (GetPageFilter) -> Unit,
        ) {
            var isAllItemsSelected: Boolean

            val dialog = DialogFilter(context)
            val binding = DialogFilterBinding.inflate(dialog.layoutInflater)
            dialog.setContentView(binding.root)

            dialog.setOnDismissListener {
            }

            val adapter = HeroAdapter(
                previouslySelected = previousFilter.heroes,
                isAllSelectedListener = { isAllSelected ->
                    binding.checkboxSelectAll.isChecked = isAllSelected
                    binding.checkboxSelectAll.jumpDrawablesToCurrentState()
                    isAllItemsSelected = isAllSelected
                }
            )

            binding.recycleViewHero.adapter = adapter
            binding.recycleViewHero.layoutManager = GridLayoutManager(
                context, 3, RecyclerView.VERTICAL, false
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

//            binding.btnNo.setOnClickListener {
//                dialog.dismiss()
//            }
//
//            binding.btnYes.setOnClickListener {
//                val text = binding.etPrompt.text.toString()
//                val heroes = adapter.getSelected()
//                val filter = GetPageFilter(text, heroes)
//                onNewSelected(filter)
//                dialog.dismiss()
//            }

            binding.btnClose.setOnClickListener {
                dialog.dismiss()
            }

            binding.btnSearch.setOnClickListener {
                val text = binding.etPrompt.text.toString()
                val heroes = adapter.getSelected()
                val filter = GetPageFilter(text, heroes)
                onNewSelected(filter)
                dialog.dismiss()
            }

            binding.etPrompt.setText(previousFilter.prompt, TextView.BufferType.EDITABLE)
            binding.etPrompt.requestFocus()

//        binding.etPrompt.setOnFocusChangeListener { v, hasFocus ->
//            if (hasFocus) {
//                //got focus
//                dialog.setCanceledOnTouchOutside(false)
//            } else {
//                //lost focus
//                dialog.setCanceledOnTouchOutside(true)
//            }
//        }

            // This listener's onShow is fired when the dialog is shown
            // This listener's onShow is fired when the dialog is shown
            dialog.setOnShowListener { _ -> // In a previous life I used this method to get handles to the positive and negative buttons
                // of a dialog in order to change their Typeface. Good ol' days.

                // This is gotten directly from the source of BottomSheetDialog
                // in the wrapInBottomSheet() method
                val bottomSheet = dialog.findViewById<View>(
                    com.google.android.material.R.id.design_bottom_sheet
                ) as? FrameLayout ?: return@setOnShowListener

                val behavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheet.setupFullHeight()
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            dialog.show()
            context.showKeyboard()

//            binding.etPrompt.doOnAttach {
//                if (dialog.ownerActivity?.window == null) {
//                    Toast.makeText(context, "dialog.window == null", Toast.LENGTH_SHORT).show()
//                }
//                dialog.ownerActivity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
//            }
//            dialog.ownerActivity?.showKeyboard()
        }
    }
}