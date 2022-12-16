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
import es.upm.isii.tapastop.adapters.TapaListAdapter
import es.upm.isii.tapastop.adapters.UsersListAdapter
import es.upm.isii.tapastop.databinding.FragmentSearchBinding
import es.upm.isii.tapastop.model.TapaViewModel
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.model.restApiStatus
import es.upm.isii.tapastop.model.userGetApiStatus

class SearchFragment : Fragment() {

	private var _binding: FragmentSearchBinding? = null

	private val binding get() = _binding!!

	private val sharedViewModel: UserViewModel by activityViewModels()
	private val tapaSharedViewModel: TapaViewModel by activityViewModels()
	private lateinit var searchET: TextInputEditText
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentSearchBinding.inflate(inflater, container, false)
		val root = binding.root
		searchET = binding.searchBarText
		binding.lifecycleOwner = this
		binding.usersList.adapter = UsersListAdapter(sharedViewModel)
		binding.tapasList.adapter = TapaListAdapter(tapaSharedViewModel, sharedViewModel)
		tapaSharedViewModel.resetStatus()
		sharedViewModel.resetStatus()
		tapaSharedViewModel.resetTapas()
		sharedViewModel.resetUsersList()
		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val nextFragment = UserProfileFragment()
		val nextTapaFragment = TapaFragment()
		binding.apply {
			viewModel = sharedViewModel
			tapaViewModel = tapaSharedViewModel
			this.lifecycleOwner?.let { it ->
				sharedViewModel.userGetStatus.observe(it) {
					when (it) {
						userGetApiStatus.LOADING -> {
							loadingLayoutUsers.visibility = View.VISIBLE
						}
						userGetApiStatus.DONE -> {
							requireActivity().supportFragmentManager.beginTransaction()
								.replace(R.id.nav_host_fragment, nextFragment, "thisfragment")
								.addToBackStack(null)
								.commit()
						}
						else -> {
							loadingLayoutUsers.visibility = View.GONE
						}
					}
				}
			}
			this.lifecycleOwner?.let {
				sharedViewModel.status.observe(it) {
					when (it) {
						restApiStatus.LOADING -> {
							loadingLayoutUsers.visibility = View.VISIBLE
						}
						restApiStatus.DONE -> {
							loadingLayoutUsers.visibility = View.GONE
						}
						restApiStatus.ERROR -> {
							loadingLayoutUsers.visibility = View.GONE
						}
						restApiStatus.NOTHING -> {}
					}
				}
			}
			this.lifecycleOwner?.let {
				tapaSharedViewModel.tapaGetStatus.observe(it) {
					when (it) {
						userGetApiStatus.LOADING -> {
							loadingLayoutTapas.visibility = View.VISIBLE
						}
						userGetApiStatus.DONE -> {
							requireActivity().supportFragmentManager.beginTransaction()
								.replace(R.id.nav_host_fragment, nextTapaFragment, "thisfragment")
								.addToBackStack(null)
								.commit()
						}
						else -> {
							loadingLayoutTapas.visibility = View.GONE
						}
					}
				}
				tapaSharedViewModel.tapas.observe(viewLifecycleOwner){
					if(it.tapas.isNullOrEmpty()){
						tapasList.visibility = View.GONE
						tapasListEmpty.visibility = View.VISIBLE
					}else{
						tapasList.visibility = View.VISIBLE
						tapasListEmpty.visibility = View.GONE
					}
				}
				sharedViewModel.users.observe(viewLifecycleOwner){
					if(it.users.isNullOrEmpty()){
						usersList.visibility = View.GONE
						usersListEmpty.visibility = View.VISIBLE
					}else{
						usersList.visibility = View.VISIBLE
						usersListEmpty.visibility = View.GONE
					}
				}
				tapaSharedViewModel.status.observe(viewLifecycleOwner) {
					when (it) {
						restApiStatus.LOADING -> {
							loadingLayoutTapas.visibility = View.VISIBLE
						}
						restApiStatus.DONE -> {
							loadingLayoutTapas.visibility = View.GONE
						}
						restApiStatus.ERROR -> {
							loadingLayoutTapas.visibility = View.GONE
						}
						restApiStatus.NOTHING -> {}
					}
				}
				searchET.addTextChangedListener {
					if (it.toString().isBlank()) {
						searchET.hint = getString(R.string.search_hint)
					} else {
						sharedViewModel.getUsers(it.toString())
						tapaSharedViewModel.getTapas(it.toString())
					}
				}
			}
		}
	}
}