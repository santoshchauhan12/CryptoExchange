package com.android.cryptocoin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.android.cryptocoin.databinding.ActivityMainBinding
import com.android.cryptocoin.ui.ExchangeFragment
import com.android.cryptocoin.util.Navigator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navigator: Navigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showFragments()

    }

    private fun showFragments() {
        val fragmentContainerId = binding.flContent.id // Replace with your actual container ID
        navigator = Navigator(supportFragmentManager, fragmentContainerId)

        val yourFragment = ExchangeFragment()
        navigator.navigateToFragment(yourFragment)
    }
}
