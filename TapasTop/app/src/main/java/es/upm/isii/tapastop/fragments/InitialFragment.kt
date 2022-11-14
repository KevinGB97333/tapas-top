package es.upm.isii.tapastop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.adapters.LoginAdapter
import es.upm.isii.tapastop.databinding.FragmentInitialBinding
import es.upm.isii.tapastop.model.UserViewModel

class InitialFragment : Fragment() {
    private var _binding : FragmentInitialBinding?= null
    private val binding get() = _binding!!
    private lateinit var loginAdapter: LoginAdapter
	private lateinit var tabLayout : TabLayout
	private lateinit var viewPager : ViewPager2

    private val sharedViewModel : UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInitialBinding.inflate(inflater, container, false)
        val root : View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loginAdapter = LoginAdapter(this)
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager
        binding.apply {
            viewModel = sharedViewModel
            viewPager.adapter = loginAdapter
            TabLayoutMediator(tabLayout, viewPager) {tab, position ->
                tab.text = when(position){
                    0 -> getText(R.string.login_title)
                    else -> getText(R.string.signup_title)
                }
            }.attach()
        }


    }
}