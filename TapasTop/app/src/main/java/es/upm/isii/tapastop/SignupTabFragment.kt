package es.upm.isii.tapastop

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import es.upm.isii.tapastop.databinding.FragmentSignupTabBinding
import es.upm.isii.tapastop.model.UserViewModel

class SignupTabFragment : Fragment() {

    // Binding object instance corresponding to the fragment_signup_tab.xml layout
	private var _binding: FragmentSignupTabBinding? = null
	private val binding get() = _binding!!

    // ViewModel object instance corresponding to the UserViewModel.kt class
    private val shareViewModel : UserViewModel by activityViewModels()

    //TextInputLayouts
	private lateinit var usernameTL: TextInputLayout
	private lateinit var emailTL: TextInputLayout
	private lateinit var passwordTL: TextInputLayout
	private lateinit var confirmPasswordTL: TextInputLayout

    //EditText
	private lateinit var usernameET: TextInputEditText
	private lateinit var emailET: TextInputEditText
	private lateinit var passwordET: TextInputEditText
	private lateinit var confirmPasswordET: TextInputEditText

    //Sign Up Button
	private lateinit var signUpBtn: Button

	private val sharedViewModel : UserViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentSignupTabBinding.inflate(inflater, container, false)
		val root = binding.root

		usernameTL = binding.signupUsername
		emailTL = binding.signupEmail
		passwordTL = binding.signupPassword
		confirmPasswordTL = binding.signupConfirmPassword
		usernameET = binding.signupUsernameText
		emailET = binding.signupEmailText
		passwordET = binding.signupPasswordText
		confirmPasswordET = binding.signupConfirmPasswordText
		signUpBtn = binding.btnSignup

		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding?.apply {


			signUpBtn.setOnClickListener {
				validateFields()
			}
			usernameET.addTextChangedListener {
				if (it.toString().isBlank()) {
					usernameTL.error = getString(R.string.empty_field_error_msg)
				} else {
					usernameTL.error = null
				}
			}
			emailET.addTextChangedListener {
				if (it.toString().isBlank()) {
					emailTL.error = getString(R.string.empty_field_error_msg)
				} else {
					emailTL.error = null

				}
			}
			passwordET.addTextChangedListener {
				if (it.toString().isBlank()) {
					passwordTL.error = getString(R.string.empty_field_error_msg)
				} else {
					passwordTL.error = null
				}
			}
			confirmPasswordET.addTextChangedListener {
				if (it.toString().isBlank()) {
					confirmPasswordTL.error = getString(R.string.empty_field_error_msg)
				} else {
					confirmPasswordTL.error = null
				}
			}
		}
	}

    /**
     * Validate all of the fields
     */
    private fun validateFields() {
		val username = usernameET.text.toString().trim()
		val email = emailET.text.toString().trim()
		val password = passwordET.text.toString().trim()
		val confirmPassword = confirmPasswordET.text.toString().trim()
		if (!checkEmptyFields(username, email, password, confirmPassword)) {

			val validUsername = isValidUsername(username)
			val emailValid = isValidEmail(email)
			if (!emailValid) {
				emailTL.error = getString(R.string.invalid_mail_msg)
			} else {
				emailTL.error = null
			}
			val passwords = isSamePassword(password, confirmPassword)
			if (!passwords) {
				confirmPasswordTL.error = getString(R.string.passwords_unmatch_msg)
			} else {
				confirmPasswordTL.error = null
			}

			if (validUsername && emailValid && passwords) {
				shareViewModel.setUsername(username)
				shareViewModel.setEmail(email)
				if (!sharedViewModel.hasGenderSet()) {
					sharedViewModel.setGender(getString(R.string.gender_male))
				}
				if (!sharedViewModel.hasProfilePicSet()) {
					ResourcesCompat.getDrawable(
						requireActivity().resources,
						R.drawable.profile_pic_male,
						null
					)
						?.let { sharedViewModel.setProfileImage(it) }
					findNavController().navigate(R.id.action_initialFragment_to_mainMenuFragment)
				}
			}
		}
	}

    /**
     * Verify if any field is empty
     *
     * @param username
     * @param email
     * @param password
     * @param confirmPassword
     */
	private fun checkEmptyFields(
		username: String,
		email: String,
		password: String,
		confirmPassword: String
	): Boolean {
		var emptyField = false
		if (username == "") {
			usernameTL.error = getString(R.string.empty_field_error_msg)
			emptyField = true
		}
		if (email == "") {
			emailTL.error = getString(R.string.empty_field_error_msg)
			emptyField = true
		}
		if (password == "") {
			passwordTL.error = getString(R.string.empty_field_error_msg)
			emptyField = true
		}
		if (confirmPassword == "") {
			confirmPasswordTL.error = getString(R.string.empty_field_error_msg)
			emptyField = true
		}
		return emptyField
	}

    /**
     * Verify if both password introduced are the same
     *
     * @param password first
     * @param confirmedPassword confirmation
     */
    private fun isSamePassword(password: String, confirmedPassword: String): Boolean {
		return password == confirmedPassword
	}

    /**
     * Validate the username introduced
     *
     * @param username to be validated
     */
    private fun isValidUsername(username: String) : Boolean{
        return username.isNotBlank()
    }

    /**
     * Verify the email introduced
     *
     * @param email to be verified
     */
    private fun isValidEmail(email : String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}