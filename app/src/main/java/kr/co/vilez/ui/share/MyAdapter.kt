package kr.co.vilez.ui.share

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class MyAdapter(val fa:FragmentActivity, var count: Int = 0) : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        val index = getRealPosition(position)
        return if (index == 0) FirstFragment()
        else if (index == 1) SecondFragment()
        else ThirdFragment()
    }

    override fun getItemCount(): Int {
        return 2000
    }

    fun getRealPosition(position: Int): Int {
        return position % count
    }

}