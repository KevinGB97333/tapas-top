package es.upm.isii.tapastop.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.hdodenhof.circleimageview.CircleImageView
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.adapters.FriendRequestListAdapter
import es.upm.isii.tapastop.adapters.UsersListAdapter
import es.upm.isii.tapastop.adapters.bindImage
import es.upm.isii.tapastop.databinding.FragmentProfileBinding
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.model.userGetApiStatus

class ProfileFragment : Fragment() {
	private var _binding: FragmentProfileBinding? = null

	private val binding get() = _binding!!

	private lateinit var profileImgIW: CircleImageView
	private lateinit var usernameTW: TextView
	private lateinit var nameTW: TextView
	private lateinit var emailTW: TextView
	private lateinit var genderTW: TextView
	private lateinit var countryTW: TextView
	private lateinit var locationTW: TextView
	private lateinit var descriptionTW: TextView
	private lateinit var degustationsTW: TextView
	private lateinit var localsTW: TextView
	private lateinit var requestsTW: TextView
	private lateinit var awardsTW: TextView

	private val sharedViewModel: UserViewModel by activityViewModels()
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentProfileBinding.inflate(inflater, container, false)
		val root = binding.root
		profileImgIW = binding.profileImg
		usernameTW = binding.usernameTextview
		nameTW = binding.nameTextview
		emailTW = binding.emailTextview
		genderTW = binding.genderTextview
		countryTW = binding.countryTextview
		locationTW = binding.locationTextview
		descriptionTW = binding.descriptionTextview
		degustationsTW = binding.degustationTextview
		localsTW = binding.localsTextview
		binding.friendsListRv.adapter = UsersListAdapter(sharedViewModel)
		binding.friendRequestsRv.adapter = FriendRequestListAdapter(
			binding.friendRequestsRv,sharedViewModel, viewLifecycleOwner, requireContext(), getString(
				R.string.try_again_msg
			)
		)
		awardsTW = binding.awardsTextview
		sharedViewModel.resetStatus()
		sharedViewModel.resetUsersList()
		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val nextFragment = UserProfileFragment()
		binding.apply {
			viewModel = sharedViewModel

			/**
			 * Save copy of the current user and navigate to edit profile fragment
			 */
			profileEdit.setOnClickListener {
				sharedViewModel.saveCurrentUser()
				findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
			}

			/**
			 * Launch new fragment to view friend profile
			 */
			sharedViewModel.userGetStatus.observe(viewLifecycleOwner) {
				when (it) {
					userGetApiStatus.DONE -> {
						requireActivity().supportFragmentManager.beginTransaction()
							.replace(R.id.nav_host_fragment, nextFragment, "thisfragment2")
							.addToBackStack(null)
							.commit()
					}
					else -> {}
				}
			}
			/**
			 * Observe friend list if it's empty or not
			 */
			sharedViewModel.friends.observe(viewLifecycleOwner) {
				if (it.users.isNullOrEmpty()) {
					friendsListRv.visibility = View.GONE
					friendsListEmpty.visibility = View.VISIBLE
				} else {
					friendsListRv.visibility = View.VISIBLE
					friendsListEmpty.visibility = View.GONE
				}
			}
			/**
			 * Observe requests list if it's empty or not
			 */
			sharedViewModel.friendRequests.observe(viewLifecycleOwner) {
				if (it.users.isNullOrEmpty()) {
					friendRequestsRv.visibility = View.GONE
					friendRequestsEmpty.visibility = View.VISIBLE
				} else {
					friendRequestsRv.visibility = View.VISIBLE
					friendRequestsEmpty.visibility = View.GONE
				}
			}
		}
	}

}