package es.upm.isii.tapastop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import es.upm.isii.tapastop.databinding.FragmentUsernameForgotPasswordBinding
import es.upm.isii.tapastop.model.UserViewModel

class UsernameForgotPasswordFragment : Fragment() {

	private var _binding : FragmentUsernameForgotPasswordBinding ?= null
	private val binding get() = _binding!!
	private val sharedViewModel : UserViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentUsernameForgotPasswordBinding.inflate(inflater,container,false)
		val root = binding.root

		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.apply {
			forgotPasswordUsernameText.addTextChangedListener{
				if(it.toString().isBlank()){
					forgotPasswordUsername.error = getString(R.string.empty_field_error_msg)
				}else{
					forgotPasswordUsername.error = null
				}
			}
			continueButton.setOnClickListener{
				if(forgotPasswordUsername.error == null){
					findNavController().navigate(R.id.action_usernameForgotPasswordFragment_to_passwordRecoveryFragment)
					sharedViewModel.sendRecoveryEmail(forgotPasswordUsernameText.text.toString())
				}
			}
		}
	}
}