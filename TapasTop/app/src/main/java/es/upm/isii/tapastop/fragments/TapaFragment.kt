package es.upm.isii.tapastop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.databinding.FragmentTapaBinding
import es.upm.isii.tapastop.model.TapaViewModel
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.model.restApiStatus

class TapaFragment : Fragment() {

	private var _binding: FragmentTapaBinding? = null
	private val binding get() = _binding!!

	private val tapaSharedViewModel: TapaViewModel by activityViewModels()
	private val userShareViewModel: UserViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentTapaBinding.inflate(inflater, container, false)

		val root = binding.root
		binding.tapaAverageRate.text = resources.getString(
			R.string.rating_text,
			tapaSharedViewModel.currentTapa.value!!.averageRate
		)
		binding.tapaRate.text = resources.getString(
			R.string.your_rating_text,
			tapaSharedViewModel.currentTapa.value!!.personalRate
		)
		binding.tapaRating.rating = tapaSharedViewModel.currentTapa.value!!.personalRate
		tapaSharedViewModel.resetStatus()
		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.apply {
			tapaViewModel = tapaSharedViewModel
			tapaRating.setOnRatingBarChangeListener { ratingBar, _, _ ->
					tapaRate.text = getString(R.string.your_rating_text, ratingBar.rating)
					tapaSharedViewModel.setUserRate(ratingBar.rating)
					tapaSharedViewModel.updateRate(userShareViewModel.currentUser.value!!.username)
			}
			lifecycleOwner?.let {
				tapaSharedViewModel.status.observe(it){
					when(it){
						restApiStatus.ERROR -> { Toast.makeText(requireContext(),resources.getString(R.string.try_again_msg),Toast.LENGTH_SHORT).show()}
						else -> {}
					}
				}
			}
		}
	}
}