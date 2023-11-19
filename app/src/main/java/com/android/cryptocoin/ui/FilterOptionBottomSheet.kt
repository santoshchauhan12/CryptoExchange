package com.android.cryptocoin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.cryptocoin.R
import com.android.cryptocoin.databinding.BottomSheetOptionsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterOptionBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetOptionsBinding

    lateinit var filterCallback: (FilterOptions) -> Unit

    fun setListeners(listener: (FilterOptions) -> Unit) {
        filterCallback = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetOptionsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {

        binding.option1.setOnClickListener {

            filterCallback.invoke(FilterOptions.PRICE)
            dismiss()
        }

        binding.option2.setOnClickListener {

            filterCallback.invoke(FilterOptions.VOLUME24H)
            dismiss()
        }

        binding.option3.setOnClickListener {

            filterCallback.invoke(FilterOptions.DEFAULT)
            dismiss()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(): FilterOptionBottomSheet {
            return  FilterOptionBottomSheet()
        }
    }

    enum class FilterOptions {
        PRICE, VOLUME24H, DEFAULT
    }
}