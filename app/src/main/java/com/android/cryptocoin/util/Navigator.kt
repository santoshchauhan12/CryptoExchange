package com.android.cryptocoin.util

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class Navigator(private val fragmentManager: FragmentManager, @IdRes private val containerId: Int) {

    fun navigateToFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(containerId, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(fragment.tag)
        }

        transaction.commit()
    }
}