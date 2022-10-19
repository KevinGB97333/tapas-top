package es.upm.isii.tapastop

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter


class LoginAdapter(fm: FragmentManager, var totalTbs: Int) :  FragmentStateAdapter(fm){
    private var totalTabs : Int = totalTbs

    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0 -> LoginTabFragment()
            1 -> SignupTabFragment()
            else -> Fragment()
        }
    }
}