package es.upm.isii.tapastop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import de.hdodenhof.circleimageview.CircleImageView
import es.upm.isii.tapastop.databinding.FragmentProfileBinding
import es.upm.isii.tapastop.model.UserViewModel

class ProfileFragment : Fragment() {
	private var _binding : FragmentProfileBinding? = null

	private val binding get()=_binding!!

	private lateinit var profileImgIW : CircleImageView
	private lateinit var usernameTW : TextView
	private lateinit var nameTW : TextView
	private lateinit var emailTW : TextView
	private lateinit var genderTW : TextView
	private lateinit var countryTW : TextView
	private lateinit var locationTW : TextView
	private lateinit var descriptionTW : TextView
	private lateinit var degustationsTW : TextView
	private lateinit var localsTW : TextView
	private lateinit var requestsTW : TextView
	private lateinit var awardsTW : TextView

	private val sharedViewModel : UserViewModel by activityViewModels()
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentProfileBinding.inflate(inflater,container,false)
		val root = binding.root
		profileImgIW = binding.profileImg
		usernameTW = binding.usernameTextview
		nameTW = binding.nameTextview
		emailTW = binding.emailTextview
		genderTW = binding.genderTextview
		countryTW = binding.countryTextview
		locationTW = binding.locationTextview
		descriptionTW = binding.descriptionTextview
		degustationsTW = binding.degustationTextview
		localsTW = binding.localsTextview
		requestsTW = binding.requestsTextview
		awardsTW = binding.awardsTextview
		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding?.apply {
			viewModel = sharedViewModel

		}
	}
}