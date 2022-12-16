package es.upm.isii.tapastop.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.view.drawToBitmap
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.databinding.FragmentAddTapaBinding
import es.upm.isii.tapastop.model.TapaViewModel
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.model.restApiStatus


class AddTapaFragment : Fragment() {

	// Binding object instance corresponding to the fragment_signup_tab.xml layout
	private var _binding : FragmentAddTapaBinding?=null
	private val binding get() = _binding!!
	private lateinit var tapaIW : ImageView
	private lateinit var tapaNameTL : TextInputLayout
	private lateinit var tapaNameET : TextInputEditText
	private lateinit var tapaRegionTL : TextInputLayout
	private lateinit var tapaRegionET : TextInputEditText
	private lateinit var tapaCountryTL : TextInputLayout
	private lateinit var tapaCountryET : TextInputEditText
	private lateinit var tapaDescriptionTL : TextInputLayout
	private lateinit var tapaDescriptionET : TextInputEditText
	private lateinit var tapaTypeTL : TextInputLayout
	private lateinit var tapaTypeAT : AutoCompleteTextView
	private lateinit var tapaTasteTL : TextInputLayout
	private lateinit var tapaTasteAT : AutoCompleteTextView
	private lateinit var tapaRestaurantTL : TextInputLayout
	private lateinit var tapaRestaurantAT : AutoCompleteTextView

	private val tapaSharedViewModel : TapaViewModel by activityViewModels()
	private val userSharedViewModel : UserViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentAddTapaBinding.inflate(inflater,container,false)
		val root = binding.root
		tapaIW = binding.tapaImg
		tapaNameTL = binding.addTapaName
		tapaNameET = binding.addTapaNameText
		tapaRegionTL = binding.region
		tapaRegionET = binding.regionText
		tapaCountryTL = binding.country
		tapaCountryET = binding.countryText
		tapaDescriptionTL = binding.description
		tapaDescriptionET = binding.descriptionText
		tapaTypeTL = binding.typesFood
		tapaTypeAT = binding.typesAutoComplete
		tapaTasteTL = binding.taste
		tapaTasteAT = binding.tasteAutoComplete
		tapaRestaurantTL = binding.restaurants
		tapaRestaurantAT = binding.restaurantsAutoComplete

		val typesFood = resources.getStringArray(R.array.types_of_food)
		val tastes = resources.getStringArray(R.array.tastes)
		val restaurantsArray = tapaSharedViewModel.restaurants.mapTo(arrayListOf()){it.name}
		val typesAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, typesFood)
		val tastesAdapter = ArrayAdapter(requireContext(),androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tastes)
		val restaurantsAdapter = ArrayAdapter(requireContext(),androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, restaurantsArray)
		binding.tapaRate.text = resources.getString(R.string.your_rating_text,tapaSharedViewModel.currentTapa.value!!.personalRate)
		binding.typesAutoComplete.setAdapter(typesAdapter)
		binding.tasteAutoComplete.setAdapter(tastesAdapter)
		binding.restaurantsAutoComplete.setAdapter(restaurantsAdapter)

		tapaSharedViewModel.resetStatus()
		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.apply {
			tapaViewModel = tapaSharedViewModel

			tapaSharedViewModel.status.observe(viewLifecycleOwner) {
				when (it) {
					restApiStatus.DONE -> {
						loadingLayout.visibility = View.GONE
						findNavController().navigate(R.id.action_addTapaFragment_to_mainMenuFragment)
					}
					restApiStatus.LOADING -> loadingLayout.visibility = View.VISIBLE
					restApiStatus.ERROR -> {
						loadingLayout.visibility = View.GONE
						Toast.makeText(
							requireContext(), getString(R.string.try_again_msg),
							Toast.LENGTH_SHORT
						)
							.show()
					}
					restApiStatus.NOTHING -> {}
				}
			}

			tapaImg.setOnClickListener{
				if (ActivityCompat.checkSelfPermission(
						requireActivity(),
						Manifest.permission.READ_EXTERNAL_STORAGE
					) != PackageManager.PERMISSION_GRANTED
				) {
					requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2000)
				} else {
					pickImageGallery()
				}
			}
			tapaRating.setOnRatingBarChangeListener { ratingBar, _, _ ->
				tapaRate.text = getString(R.string.your_rating_text,ratingBar.rating)
				tapaSharedViewModel.setUserRate(ratingBar.rating)
			}
			tapaNameET.addTextChangedListener {
				if(it.toString().isBlank()){
					tapaNameTL.error = getString(R.string.empty_field_error_msg)
				}else{
					tapaNameTL.error = null
				}
			}


			tapaTypeAT.setOnItemClickListener { _, _, _, _ -> tapaTypeTL.error = null }
			tapaTasteAT.setOnItemClickListener { _, _, _, _ -> tapaTasteTL.error = null }
			tapaRestaurantAT.setOnItemClickListener { _, _, _, _ -> tapaRestaurantTL.error = null }

			tapaTypeAT.addTextChangedListener {
				if(it.toString().isBlank()){
					tapaTypeTL.error = getString(R.string.empty_field_error_msg)
				}
			}
			tapaTasteAT.addTextChangedListener {
				if(it.toString().isBlank()){
					tapaTasteTL.error = getString(R.string.empty_field_error_msg)
				}
			}
			tapaRestaurantAT.addTextChangedListener {
				if(it.toString().isBlank()){
					tapaRestaurantTL.error = getString(R.string.empty_field_error_msg)
				}
			}
			addTapa.setOnClickListener{
				checkFields()
			}
		}
	}
	private fun checkFields(){
		if(tapaNameET.text!!.isBlank()){
			tapaNameTL.error = getString(R.string.empty_field_error_msg)
		}
		if(tapaTypeAT.text!!.isBlank()){
			tapaTypeTL.error = getString(R.string.empty_field_error_msg)
		}
		if(tapaTasteAT.text!!.isBlank()){
			tapaTasteTL.error = getString(R.string.empty_field_error_msg)
		}
		if(tapaRestaurantAT.text!!.isBlank()){
			tapaRestaurantTL.error = getString(R.string.empty_field_error_msg)
		}
		if(tapaNameTL.error == null && tapaTypeTL.error == null && tapaTasteTL.error == null && tapaRestaurantTL.error == null){
			setTapa()
			tapaSharedViewModel.createTapa()
		}
	}
	private fun setTapa(){
		tapaSharedViewModel.setTapaImage(image = tapaIW.drawToBitmap())
		tapaSharedViewModel.setAuthor(author = userSharedViewModel.currentUser.value!!.username)
		tapaSharedViewModel.setName(name = tapaNameET.text.toString())
		tapaSharedViewModel.setType(type = tapaTypeAT.text.toString())
		tapaSharedViewModel.setTaste(taste = tapaTasteAT.text.toString())
		tapaSharedViewModel.setRegion(region = tapaRegionET.text.toString())
		tapaSharedViewModel.setCountry(country = tapaCountryET.text.toString())
		tapaSharedViewModel.setRestaurant(restaurant = tapaRestaurantAT.text.toString())
		tapaSharedViewModel.setDescription(description = tapaDescriptionET.text.toString())
		tapaSharedViewModel.setDate()
	}
	private fun pickImageGallery() {
		val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
		startActivityForResult(cameraIntent, PostSignUpFragment.IMAGE_REQUEST_CODE)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 100) {
				val returnUri = data?.data
				tapaIW.setImageURI(returnUri)
			}
		}
	}
}