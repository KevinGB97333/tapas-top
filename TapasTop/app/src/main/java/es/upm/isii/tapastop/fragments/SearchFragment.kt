package es.upm.isii.tapastop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.adapters.UsersListAdapter
import es.upm.isii.tapastop.databinding.FragmentSearchBinding
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.model.restApiStatus
import es.upm.isii.tapastop.model.userGetApiStatus

class SearchFragment : Fragment() {

	private var _binding : FragmentSearchBinding ?= null

	private val binding get() = _binding!!

	private val sharedViewModel : UserViewModel by activityViewModels()
	private lateinit var searchET : TextInputEditText
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentSearchBinding.inflate(inflater,container,false)
		val root = binding.root
		searchET = binding.searchBarText
		binding.lifecycleOwner = this
		binding.usersList.adapter = UsersListAdapter(sharedViewModel)
		sharedViewModel.resetStatus()
		sharedViewModel.resetUsersList()
		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val nextFragment = UserProfileFragment()
		binding.apply {
			viewModel = sharedViewModel
			this.lifecycleOwner?.let { it ->
				sharedViewModel.userGetStatus.observe(it){
					when(it){
						userGetApiStatus.LOADING -> {
							loadingLayout.visibility = View.VISIBLE
						}
						userGetApiStatus.DONE -> {
							requireActivity().supportFragmentManager.beginTransaction()
								.replace(R.id.nav_host_fragment,nextFragment,"thisfragment")
								.addToBackStack(null)
								.commit()
						}
						else -> {loadingLayout.visibility = View.GONE}
					}
				}
			}
			this.lifecycleOwner?.let {
				sharedViewModel.status.observe(it){
					when(it){
						restApiStatus.LOADING -> {
							loadingLayout.visibility = View.VISIBLE
						}
						restApiStatus.DONE -> {
							loadingLayout.visibility = View.GONE
						}
						restApiStatus.ERROR -> {
							loadingLayout.visibility = View.GONE
						}
						restApiStatus.NOTHING -> {}
					}
				}
			}
			searchET.addTextChangedListener{
				if(it.toString().isBlank()){
					searchET.hint = getString(R.string.search_hint)
				}else{
					sharedViewModel.getUsers(it.toString())
				}
			}
		}
	}

}