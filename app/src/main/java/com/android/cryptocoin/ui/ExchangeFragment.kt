package com.android.cryptocoin.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cryptocoin.databinding.FragmentExchangeBinding
import com.android.cryptocoin.model.bitcoin.CryptoItem
import com.android.cryptocoin.viewmodel.ExchangeViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch


class ExchangeFragment : Fragment() {

    private var binding : FragmentExchangeBinding?= null
    lateinit var exchangeViewModel: ExchangeViewModel

    var exchangeList : MutableList<CryptoItem> ? = null
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
        getShoppingList()
    }

    private fun initUi() {

        binding?.rvShoppingList?.layoutManager =  LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun getShoppingList() {
        exchangeViewModel.bitCoinListLiveData.observe(this.requireActivity()) { cryptoList ->

            exchangeList = cryptoList as MutableList<CryptoItem>?

            var cryptoitem = fetchAndRemoveFirstItem(exchangeList)
            setTopBanner(cryptoitem)
            val exchangeListAdapter = ExchangeListAdapter(exchangeList)
            binding?.rvShoppingList?.adapter = exchangeListAdapter

//            binding?.rvShoppingList?.adapter?.notifyDataSetChanged()
            Log.e("cryptocurr", "cryptolist " + cryptoList)

        }

        this.lifecycleScope.launch {
            exchangeViewModel.getBitCoinList()
        }
    }

    fun setTopBanner(cryptoItem: CryptoItem?){
        binding?.tvName?.text =  cryptoItem?.name
        binding?.tvSymbol?.text =  cryptoItem?.symbol
        binding?.tvPrice?.text =  cryptoItem?.quote?.USD?.price.toString()
        binding?.tvPercentStatus?.text = cryptoItem?.quote?.USD?.percentChange24h.toString().plus("%")
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