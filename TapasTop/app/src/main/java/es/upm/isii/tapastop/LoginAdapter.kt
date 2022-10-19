package es.upm.isii.tapastop

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter


class LoginAdapter(fm: Fragment) :  FragmentStateAdapter(fm){

    override fun getItemCount() : Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> LoginTabFragment()
            else -> SignupTabFragment()
        }
    }
}