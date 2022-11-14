package es.upm.isii.tapastop.fragments

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.databinding.FragmentMainMenuBinding
import es.upm.isii.tapastop.model.UserViewModel


class MainMenuFragment : Fragment() {

	// Binding object instance corresponding to the fragment_main_menu.xml layout
	private var _binding: FragmentMainMenuBinding? = null
	private val binding get() = _binding!!

	// ViewModel object instance corresponding to the UserViewModel.kt class
	private val sharedViewModel: UserViewModel by activityViewModels()
	private lateinit var viewProfileBtn: Button
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentMainMenuBinding.inflate(inflater, container, false)
		val root = binding.root
		viewProfileBtn = binding.viewProfileBtn

		sharedViewModel.resetStatus()
		sharedViewModel.getFriendRequestsUpdate()
		sharedViewModel.getFriendsUpdate()
		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		super.onViewCreated(view, savedInstanceState)
		val dialog = MaterialAlertDialogBuilder(
			ContextThemeWrapper(
				requireContext(),
				R.style.AlertDialogCustom
			)
		)
		dialog.setTitle(getString(R.string.help_title))
			.setMessage(getString(R.string.help_text))
			.setPositiveButton(getString(R.string.ok_text)) { dialog, _ ->
				dialog.cancel()
			}
		binding.apply {
			viewModel = sharedViewModel
			logoutImg.setOnClickListener {
				sharedViewModel.resetUser()
				Toast.makeText(requireContext(), "Successful logout", Toast.LENGTH_SHORT).show()
				findNavController().navigate(R.id.action_mainMenuFragment_to_initialFragment)
			}
			helpBtn.setOnClickListener {
				dialog.show()
			}
			searchImg.setOnClickListener {
				findNavController().navigate(R.id.action_mainMenuFragment_to_searchFragment)
			}
			viewProfileBtn.setOnClickListener {
				findNavController().navigate(R.id.action_mainMenuFragment_to_profileFragment)
			}
		}
	}
}