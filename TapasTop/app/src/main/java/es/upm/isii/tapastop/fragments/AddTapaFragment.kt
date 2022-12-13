package es.upm.isii.tapastop.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.databinding.FragmentAddTapaBinding
import es.upm.isii.tapastop.model.TapaViewModel
import es.upm.isii.tapastop.network.Restaurant

class AddTapaFragment : Fragment() {

	// Binding object instance corresponding to the fragment_signup_tab.xml layout
	private var _binding : FragmentAddTapaBinding?=null
	private val binding get() = _binding!!


	private val tapaSharedViewModel : TapaViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentAddTapaBinding.inflate(inflater,container,false)
		val root = binding.root
		val typesFood = resources.getStringArray(R.array.types_of_food)
		val tastes = resources.getStringArray(R.array.tastes)
		val restaurantsArray = tapaSharedViewModel.restaurants.mapTo(arrayListOf()){it.name}
		Log.d("Restaurants", "${restaurantsArray.toString()}, ${tapaSharedViewModel.restaurants}")
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

			tapaRating.setOnRatingBarChangeListener { ratingBar, _, _ ->
				tapaSharedViewModel.setUserRate(ratingBar.rating)
			}
			addTapa.setOnClickListener {
				tapaSharedViewModel.createTapa()
			}
		}
	}
}