package es.upm.isii.tapastop.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.databinding.FragmentAgeValidationBinding
import java.util.*


class AgeValidationFragment : Fragment() {
	private var _binding: FragmentAgeValidationBinding? = null
	private val binding get() = _binding!!

	private lateinit var datePicker: DatePicker

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentAgeValidationBinding.inflate(inflater, container, false)
		val root = binding.root
		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val calendar: Calendar = Calendar.getInstance()
		datePicker = binding.agePicker

		/**
		 * Ask for image storage permissions
		 */
		if (ActivityCompat.checkSelfPermission(
				requireActivity(),
				Manifest.permission.READ_EXTERNAL_STORAGE
			) != PackageManager.PERMISSION_GRANTED
		) {
			requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2000)
		}
		calendar.add(Calendar.YEAR, -18)
		datePicker.maxDate = calendar.timeInMillis
		binding.apply {
			nextFb.setOnClickListener {
				findNavController().navigate(R.id.action_ageValidationFragment_to_initialFragment)
			}
		}
	}
}