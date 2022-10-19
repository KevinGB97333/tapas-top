package es.upm.isii.tapastop

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class LoginAdapter(private val fm : FragmentManager, var totalTbs : Int) :  FragmentPagerAdapter(fm){
    private var totalTabs : Int = totalTbs
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        var fragment : Fragment = Fragment()
        when(position){
            0 -> fragment =  LoginTabFragment()
            1 -> fragment = SignupTabFragment()
        }
        return fragment
    }
}