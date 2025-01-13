package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BlankFragment()
            1 -> BlankFragment2()
            2 -> BlankFragment3()
            3 -> BlankFragment4()
            4 -> BlankFragment5()
            else -> BlankFragment()
        }
    }
}