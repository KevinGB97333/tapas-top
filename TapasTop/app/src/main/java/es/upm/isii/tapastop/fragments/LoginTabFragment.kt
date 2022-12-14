package es.upm.isii.tapastop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.databinding.FragmentLoginTabBinding
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.model.restApiStatus

class LoginTabFragment : Fragment() {

	private var _binding: FragmentLoginTabBinding? = null

	private val binding get() = _binding!!

	private val sharedViewModel: UserViewModel by activityViewModels()

	//TextInputLayout
	private lateinit var usernameTL: TextInputLayout
	private lateinit var passwordTL: TextInputLayout

	//EditText
	private lateinit var usernameET: TextInputEditText
	private lateinit var passwordET: TextInputEditText

	private lateinit var forgotPassword: TextView
	private lateinit var loginBtn: Button


	//Animations
	private lateinit var usernameAnimation: Animation
	private lateinit var passwordAnimation: Animation
	private lateinit var loginAnimation: Animation

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentLoginTabBinding.inflate(inflater, container, false)
		val root = binding.root
		usernameTL = binding.loginUsername
		passwordTL = binding.loginPassword
		forgotPassword = binding.forgotPassword
		loginBtn = binding.btnLogin

		usernameET = binding.loginUsernameText
		passwordET = binding.loginPasswordText
		//Animations
		usernameAnimation = AnimationUtils.loadAnimation(activity, R.anim.start_username_field_anim)
		passwordAnimation = AnimationUtils.loadAnimation(activity, R.anim.start_password_field_anim)
		loginAnimation = AnimationUtils.loadAnimation(activity, R.anim.start_button_anim)

		usernameTL.startAnimation(usernameAnimation)
		passwordTL.startAnimation(passwordAnimation)
		forgotPassword.startAnimation(passwordAnimation)
		loginBtn.startAnimation(loginAnimation)
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
						loadingLayout.visibility = View.GONE
						findNavController().navigate(R.id.action_initialFragment_to_mainMenuFragment)
					}
					restApiStatus.LOADING -> loadingLayout.visibility = View.VISIBLE
					restApiStatus.ERROR -> {
						loadingLayout.visibility = View.GONE
						Toast.makeText(
							requireContext(),
							getString(R.string.login_error_msg),
							Toast.LENGTH_SHORT
						)
							.show()
					}
					restApiStatus.NOTHING -> {}
				}
			}
			loginBtn.setOnClickListener {
				validateFields()
			}
			usernameET.addTextChangedListener {
				usernameET.addTextChangedListener {
					if (it.toString().isBlank()) {
						usernameTL.error = getString(R.string.empty_field_error_msg)
					} else {
						usernameTL.error = null
					}
				}
			}
			passwordET.addTextChangedListener {
				if (it.toString().isBlank()) {
					passwordTL.error = getString(R.string.empty_field_error_msg)
				} else {
					passwordTL.error = null
				}
			}
			forgotPassword.setOnClickListener {
				findNavController().navigate(R.id.action_initialFragment_to_usernameForgotPasswordFragment)
			}
		}
	}

	private fun validateFields() {
		if (passwordTL.error == null && usernameTL.error == null) {
			sharedViewModel.getUser(
				usernameET.text.toString().trim(),
				passwordET.text.toString().trim()
			)
		}
	}
}