package es.upm.isii.tapastop

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.mikhaellopez.circularimageview.CircularImageView
import es.upm.isii.tapastop.PostSignUpFragment.Companion.IMAGE_REQUEST_CODE
import es.upm.isii.tapastop.databinding.FragmentPostSignupBinding
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.model.restApiStatus
import kotlin.math.round


class PostSignUpFragment : Fragment() {
	companion object {
		val IMAGE_REQUEST_CODE = 100
	}

	private var _binding: FragmentPostSignupBinding? = null
	private val binding get() = _binding!!

	// ViewModel object instance corresponding to the UserViewModel.kt class
	private val shareViewModel : UserViewModel by activityViewModels()

	private lateinit var profileImgIW: CircularImageView

	private lateinit var nameET : TextInputEditText
	private lateinit var lastNameET : TextInputEditText
	private lateinit var countryET : TextInputEditText
	private lateinit var locationET : TextInputEditText
	private lateinit var descriptionET : TextInputEditText

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentPostSignupBinding.inflate(inflater, container, false)
		val root = binding.root
		profileImgIW = binding.profileImg
		nameET = binding.signUpNameText
		lastNameET = binding.signUpLastNameText
		countryET = binding.signUpCountryText
		locationET = binding.signUpLocationText
		descriptionET = binding.signUpDescriptionText
		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.apply {
			viewModel = shareViewModel
			shareViewModel.status.observe(viewLifecycleOwner){
				when(it){
					restApiStatus.DONE -> {loadingLayout.visibility = View.GONE
						findNavController().navigate(R.id.action_postSignUpFragment_to_mainMenuFragment)
					}
					restApiStatus.LOADING -> loadingLayout.visibility = View.VISIBLE
					restApiStatus.ERROR -> { loadingLayout.visibility = View.GONE
						Toast.makeText(requireContext(), "Error al completar el registro",
							Toast.LENGTH_SHORT)
							.show()
					}
					restApiStatus.NOTHING -> {}
				}
			}
			profileImg.setOnClickListener {
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
			nextScreen.setOnClickListener{
				setUserValues()
				 shareViewModel.createUser()
			}
		}
	}
	private fun setUserValues(){
		shareViewModel.setProfileImage(profileImgIW.drawToBitmap())
		shareViewModel.setName(nameET.text.toString().trim())
		shareViewModel.setLastName(lastNameET.text.toString().trim())
		shareViewModel.setCountry(countryET.text.toString().trim())
		shareViewModel.setLocation(locationET.text.toString().trim())
		shareViewModel.setDescription(descriptionET.text.toString().trim())
	}
	private fun pickImageGallery() {
		val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
		startActivityForResult(cameraIntent, IMAGE_REQUEST_CODE)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 100) {
				val returnUri = data?.data;
				profileImgIW.setImageURI(returnUri);
			}
		}
	}
}
