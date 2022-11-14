package es.upm.isii.tapastop

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.view.drawToBitmap
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mikhaellopez.circularimageview.CircularImageView
import es.upm.isii.tapastop.databinding.FragmentEditProfileBinding
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.model.restApiStatus
import es.upm.isii.tapastop.model.usernameAvailability
import es.upm.isii.tapastop.network.User
import org.w3c.dom.Text

class EditProfileFragment : Fragment() {

	private var _binding : FragmentEditProfileBinding ?= null

	private val binding get() = _binding!!

	private val sharedViewModel : UserViewModel by activityViewModels()
	private lateinit var profileIW : CircularImageView
	private lateinit var usernameTL : TextInputLayout
	private lateinit var  usernameET : TextInputEditText
	private lateinit var emailTL : TextInputLayout
	private lateinit var  emailET : TextInputEditText
	private lateinit var passwordTL : TextInputLayout
	private lateinit var passwordET : TextInputEditText
	private lateinit var confirmPasswordTL : TextInputLayout
	private lateinit var confirmPasswordET : TextInputEditText
	private lateinit var nameET : TextInputEditText
	private lateinit var surnameET : TextInputEditText
	private lateinit var countryET : TextInputEditText
	private lateinit var locationET : TextInputEditText
	private lateinit var descriptionET : TextInputEditText

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentEditProfileBinding.inflate(inflater,container, false)
		val root = binding.root
		usernameTL = binding.editProfileUsername
		usernameET = binding.editProfileUsernameText
		emailTL = binding.editProfileEmail
		emailET = binding.editProfileEmailText
		passwordTL = binding.editProfilePassword
		passwordET = binding.editProfilePasswordText
		confirmPasswordTL = binding.editProfileConfirmPassword
		confirmPasswordET = binding.editProfileConfirmPasswordText
		nameET = binding.editProfileNameText
		surnameET = binding.editProfileLastNameText
		countryET = binding.editProfileCountryText
		locationET = binding.editProfileLocationText
		descriptionET = binding.editProfileDescriptionText
		profileIW = binding.editProfileImg
		sharedViewModel.resetStatus()
		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.apply {
			viewModel = sharedViewModel
			sharedViewModel.status.observe(viewLifecycleOwner){
				when(it){
					restApiStatus.DONE -> {
						loadingLayout.visibility = View.GONE
						Toast.makeText(requireContext(),getString(R.string.update_msg),Toast.LENGTH_SHORT).show()
						findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
					}
					restApiStatus.LOADING -> { loadingLayout.visibility = View.VISIBLE
					}
					restApiStatus.ERROR -> {
						Toast.makeText(requireContext(),getString(R.string.try_again_msg),Toast.LENGTH_SHORT).show()
						loadingLayout.visibility = View.GONE
					}
					restApiStatus.NOTHING -> {
					}
				}
			}
			sharedViewModel.checkUsernameStatus.observe(viewLifecycleOwner){
				when(it){
					usernameAvailability.AVAILABLE -> {
						loadingLayout.visibility = View.GONE
						usernameTL.endIconMode = TextInputLayout.END_ICON_CUSTOM
						usernameTL.error = null
					}
					usernameAvailability.LOADING ->{
						loadingLayout.visibility = View.VISIBLE
						Toast.makeText(requireContext(),getString(R.string.check_name_availability_msg),Toast.LENGTH_SHORT).show()
						usernameTL.endIconMode = TextInputLayout.END_ICON_NONE
					}
					usernameAvailability.EXIST -> {
						loadingLayout.visibility = View.GONE
						usernameTL.error = getString(R.string.username_exist_error_msg)
						usernameTL.endIconMode = TextInputLayout.END_ICON_NONE
					}
					usernameAvailability.ERROR -> {
						loadingLayout.visibility = View.GONE
						usernameTL.error = null
						usernameTL.endIconMode = TextInputLayout.END_ICON_NONE
					}
					usernameAvailability.NOTHING -> {}
				}
			}

			usernameET.addTextChangedListener {
				if (it.toString().isBlank()) {
					usernameTL.error = getString(R.string.empty_field_error_msg)
				}else if(it.toString() == sharedViewModel.currentUser.value!!.username.toString()) {
					usernameTL.error = null
					usernameTL.endIconMode = TextInputLayout.END_ICON_CUSTOM
				}else{
					usernameTL.error = null
					usernameTL.endIconMode = TextInputLayout.END_ICON_NONE
				}
			}
			emailET.addTextChangedListener {
				if(it.toString().isBlank()){
					emailTL.error = getString(R.string.empty_field_error_msg)
				}else if(!Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()){
					emailTL.error = getString(R.string.invalid_mail_msg)
				}else{
					emailTL.error = null
				}
			}
			passwordET.addTextChangedListener {
				if(it.toString().isBlank()){
					passwordTL.error = getString(R.string.empty_field_error_msg)
				}else{
					if(confirmPasswordET.text.toString().isNotBlank() && it.toString() != confirmPasswordET.text.toString()) {
						confirmPasswordTL.error = getString(R.string.passwords_unmatch_msg)
					}
					if(it.toString() == confirmPasswordET.text.toString()){
						confirmPasswordTL.error = null
					}
					passwordTL.error = null
				}
			}
			confirmPasswordET.addTextChangedListener {
				if(it.toString().isBlank() || ( passwordET.text.toString().isNotBlank() && it.toString() != passwordET.text.toString())){
					confirmPasswordTL.error = getString(R.string.passwords_unmatch_msg)
				}else if(it.toString() == passwordET.text.toString()){
					confirmPasswordTL.error = null
				}
			}
			editProfileImg.setOnClickListener{
				if (ActivityCompat.checkSelfPermission(
						requireActivity(),
						Manifest.permission.READ_EXTERNAL_STORAGE
					) != PackageManager.PERMISSION_GRANTED
				) {
					requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2000);
				} else {
					pickImageGallery();
				}
			}
			editProfileSave.setOnClickListener{
				isValidUsername()
				checkFields()
			}
		}
	}
	private fun checkFields(){
		if(usernameTL.endIconMode == TextInputLayout.END_ICON_CUSTOM && emailTL.error == null && passwordTL.error == null && confirmPasswordTL.error == null){
			setUser()
			sharedViewModel.updateUser()
		}
	}
	private fun setUser(){
		sharedViewModel.setProfileImage(image = profileIW.drawToBitmap())
		sharedViewModel.setUsername(username = usernameET.text.toString())
		sharedViewModel.setUserEmail(email = emailET.text.toString())
		sharedViewModel.setUserPassword(password = passwordET.text.toString())
		sharedViewModel.setName(name = nameET.text.toString())
		sharedViewModel.setLastName(lastName = surnameET.text.toString())
		sharedViewModel.setCountry(country = countryET.text.toString())
		sharedViewModel.setLocation(location = locationET.text.toString())
		sharedViewModel.setDescription(description = descriptionET.text.toString())
	}
	private fun isValidUsername(){
		val username = usernameET.text.toString()
		if(usernameTL.error == null  && usernameTL.endIconMode != TextInputLayout.END_ICON_CUSTOM && username != sharedViewModel.userBackup.username){
			sharedViewModel.checkUsername(username)
		}
	}
	private fun pickImageGallery() {
		val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
		startActivityForResult(cameraIntent, PostSignUpFragment.IMAGE_REQUEST_CODE)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 100) {
				val returnUri = data?.data;
				profileIW.setImageURI(returnUri);
			}
		}
	}
}