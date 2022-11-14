package es.upm.isii.tapastop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.databinding.FragmentUserProfileBinding
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.model.restApiStatus

class UserProfileFragment : Fragment() {

	private var _binding: FragmentUserProfileBinding? = null
	private val binding get() = _binding!!

	private val sharedViewModel: UserViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentUserProfileBinding.inflate(inflater, container, false)
		val root = binding.root
		sharedViewModel.resetStatus()
		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.apply {
			viewModel = sharedViewModel

			sharedViewModel.status.observe(viewLifecycleOwner) {
				when (it) {
					restApiStatus.DONE -> {
						requestActionBtn.visibility = View.GONE
						Toast.makeText(
							requireContext(),
							getString(R.string.pending_friend_request),
							Toast.LENGTH_SHORT
						).show()
					}
					else -> {
					}
				}
			}
			requestActionBtn.setOnClickListener {
				if (!sharedViewModel.requested.contains(usernameTextview.text.toString())) {
					sharedViewModel.sendFriendRequest()
				}
			}
		}

	}
}