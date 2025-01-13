package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager2)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        viewPager.adapter = PageAdapter(this)

        // 将TabLayout與ViewPager2連接
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "主頁"
                1 -> tab.text = "運行情報"
                2 -> tab.text = "路線查詢"
                3 -> tab.text = "車次查詢"
                4 -> tab.text = "車站查詢"
            }
        }.attach()
    }

    inner class PageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
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
}