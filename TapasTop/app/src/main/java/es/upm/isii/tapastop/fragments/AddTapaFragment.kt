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
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.databinding.FragmentAddTapaBinding
import es.upm.isii.tapastop.model.TapaViewModel
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.network.Restaurant

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
	private lateinit var tapaTypeAT : AutoCompleteTextView
	private lateinit var tapaTasteAT : AutoCompleteTextView
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
		tapaTypeAT = binding.typesAutoComplete
		tapaTasteAT = binding.tasteAutoComplete
		tapaRestaurantAT = binding.restaurantsAutoComplete

		val typesFood = resources.getStringArray(R.array.types_of_food)
		val tastes = resources.getStringArray(R.array.tastes)
		val restaurantsArray = tapaSharedViewModel.restaurants.mapTo(arrayListOf()){it.name}
		val typesAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, typesFood)
		val tastesAdapter = ArrayAdapter(requireContext(),androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tastes)
		val restaurantsAdapter = ArrayAdapter(requireContext(),androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, restaurantsArray)
		binding.tapaRate.text = resources.getString(R.string.your_rating_text,0.0)
		binding.typesAutoComplete.setAdapter(typesAdapter)
		binding.tasteAutoComplete.setAdapter(tastesAdapter)
		binding.restaurantsAutoComplete.setAdapter(restaurantsAdapter)
		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.apply {
			tapaViewModel = tapaSharedViewModel
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
				tapaSharedViewModel.setUserRate(ratingBar.rating)
			}
			addTapa.setOnClickListener {
				tapaSharedViewModel.createTapa()
			}
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