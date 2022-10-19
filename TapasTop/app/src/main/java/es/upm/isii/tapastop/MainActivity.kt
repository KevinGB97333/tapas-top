package es.upm.isii.tapastop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import es.upm.isii.tapastop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	private lateinit var binding : ActivityMainBinding
	private lateinit var tabLayout : TabLayout
	private lateinit var viewPager : ViewPager2
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		tabLayout = binding.tabLayout
		viewPager = binding.viewPager
		tabLayout.addTab(tabLayout.newTab().setText(R.string.login_title))
		tabLayout.addTab(tabLayout.newTab().setText(R.string.signup_title))
		tabLayout.tabGravity = TabLayout.GRAVITY_FILL
		val loginAdapter = LoginAdapter(supportFragmentManager, tabLayout.tabCount)
		viewPager.adapter =
		viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

	}
}