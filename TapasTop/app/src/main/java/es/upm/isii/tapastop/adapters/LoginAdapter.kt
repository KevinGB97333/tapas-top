package es.upm.isii.tapastop.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import es.upm.isii.tapastop.LoginTabFragment
import es.upm.isii.tapastop.SignupTabFragment


class LoginAdapter(fm: Fragment) :  FragmentStateAdapter(fm){

    override fun getItemCount() : Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> LoginTabFragment()
            else -> SignupTabFragment()
        }
    }
}