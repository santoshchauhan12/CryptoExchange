package com.android.cryptocoin.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cryptocoin.databinding.CustomTabBinding
import com.android.cryptocoin.databinding.FragmentExchangeBinding
import com.android.cryptocoin.model.bitcoin.CryptoItem
import com.android.cryptocoin.util.Utils
import com.android.cryptocoin.viewmodel.ExchangeViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch


class ExchangeFragment : Fragment() {

    private var binding : FragmentExchangeBinding?= null
    lateinit var exchangeViewModel: ExchangeViewModel

    var exchangeList : MutableList<CryptoItem> ? = null
    var exchangeListAdapter : ExchangeListAdapter ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExchangeBinding.inflate(inflater, container, false)

        return binding?.root


        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModels()
        initUi()
        initObservers()
        initTab()
        setListeners()
        getCryptoExchangeList()
    }

    private fun initObservers() {
        exchangeViewModel.bitCoinListLiveData.observe(this.requireActivity()) { cryptoList ->

            exchangeList = cryptoList as MutableList<CryptoItem>?

            val cryptoitem = fetchAndRemoveFirstItem(exchangeList)
            setTopBanner(cryptoitem)
            exchangeListAdapter = ExchangeListAdapter(exchangeList)
            binding?.rvShoppingList?.adapter = exchangeListAdapter

//            binding?.rvShoppingList?.adapter?.notifyDataSetChanged()
            Log.e("cryptocurr", "cryptolist " + cryptoList)

        }

        exchangeViewModel.showError.observe(this.requireActivity()) {
            Toast.makeText(this.context, it, Toast.LENGTH_LONG).show()
        }

        exchangeViewModel.loading.observe(this.requireActivity()) {
            if(it == true) {
                binding?.progressBar?.visibility = View.VISIBLE
            } else {
                binding?.progressBar?.visibility = View.GONE

            }
        }
    }

    private fun setListeners() {
        binding?.etSearch?.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                performSearch(binding?.etSearch?.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }

        binding?.llFilter?.setOnClickListener {
            showOptionBottomSheet()
        }
    }

    private fun showOptionBottomSheet() {

        val filterBottomSheet = FilterOptionBottomSheet.newInstance().apply {
            setListeners {filterOption ->

                when(filterOption) {

                     FilterOptionBottomSheet.FilterOptions.PRICE -> {

                         val priceSortList = exchangeList
                         priceSortList?.sortBy { it.quote.USD.price }
                         priceSortList?.let { exchangeListAdapter?.updateList(it) }

                     }

                    FilterOptionBottomSheet.FilterOptions.VOLUME24H -> {
                        val volume24ChangeList = exchangeList
                        volume24ChangeList?.sortBy { it.quote.USD.volumeChange24h }
                        volume24ChangeList?.let { exchangeListAdapter?.updateList(it) }
                    }

                    FilterOptionBottomSheet.FilterOptions.DEFAULT -> {
                        val defaultList = exchangeList

                        defaultList?.let { exchangeListAdapter?.updateList(it) }
                    }
                }
            }
        }

        filterBottomSheet.show(childFragmentManager, "filter bottom sheet")
    }

    private fun performSearch(query: String) {

        exchangeListAdapter?.filter(query)
    }

    private fun initTab() {
        binding?.tabLayout?.let { addTab(it, "Cryptocurrency") };
        binding?.tabLayout?.let { addTab(it, "NFT") };

        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                updateTabTextColor(tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                updateTabTextColor(tab, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Do something when a tab is reselected
            }
        })
        val firstTab = binding?.tabLayout?.getTabAt(0)
        firstTab?.select()

    }

    private fun updateTabTextColor(tab: TabLayout.Tab, isSelected: Boolean) {
        val tabView = tab.customView as TextView
        if (isSelected) {
            tabView.setTextColor(Color.BLACK)  // Set color for selected tab
        } else {
            tabView.setTextColor(Color.GRAY)   // Set color for unselected tab
        }
    }

    private fun initUi() {

        binding?.rvShoppingList?.layoutManager =  LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun addTab(tabLayout: TabLayout, title: String) {
        // Create a custom view for the tab
        val customTabBinding = CustomTabBinding.inflate(LayoutInflater.from(this.context))
                customTabBinding.tvTab.text = title

        // Add the custom view as a tab
        tabLayout.addTab(tabLayout.newTab().setCustomView(customTabBinding.tvTab))
    }

    private fun getCryptoExchangeList() {
        this.lifecycleScope.launch {
            exchangeViewModel.getBitCoinList()
        }
    }

    fun setTopBanner(cryptoItem: CryptoItem?){
        binding?.tvName?.text =  cryptoItem?.name
        binding?.tvSymbol?.text =  cryptoItem?.symbol
        binding?.tvPrice?.text =
            cryptoItem?.quote?.USD?.price?.toInt().toString().let {
                Utils.prependDollarUsingConcatenation(
                    it
                )
            }.plus( " USD")
        binding?.tvPercentStatus?.text = cryptoItem?.quote?.USD?.percentChange24h?.let {
            Utils.formatDoubleToOneDecimal(
                it
            ).plus("%")
        }
        context?.let { binding?.ivTopCurrencyLogo?.let { it1 ->
            Glide.with(it).load(cryptoItem?.logoUrl).into(
                it1
            )
        } }

    }
    fun fetchAndRemoveFirstItem(myList: MutableList<CryptoItem>?): CryptoItem? {
        if (myList?.isNotEmpty() == true) {
            // Fetch the first item
            val firstItem = myList[0]

            // Remove the first item
            myList.removeAt(0)

            return firstItem
        } else {
            // The list is empty, return null or handle accordingly
            return null
        }
    }


    private fun initViewModels() {
        exchangeViewModel = ExchangeViewModel()
    }
}