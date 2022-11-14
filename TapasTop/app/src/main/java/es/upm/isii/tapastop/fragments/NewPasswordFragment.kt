package es.upm.isii.tapastop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.databinding.FragmentNewPasswordBinding
import es.upm.isii.tapastop.model.UserViewModel
import es.upm.isii.tapastop.model.restApiStatus

class NewPasswordFragment : Fragment() {
	private var _binding : FragmentNewPasswordBinding ?= null
	private val binding get() = _binding!!
	private val sharedViewModel :UserViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentNewPasswordBinding.inflate(inflater,container,false)
		val root = binding.root
		return root
		sharedViewModel.resetStatus()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.apply {

			sharedViewModel.status.observe(viewLifecycleOwner){
				when(it){
					restApiStatus.DONE -> {
						loadingLayout.visibility = View.GONE
						Toast.makeText(requireContext(),getString(R.string.update_msg), Toast.LENGTH_SHORT).show()
						findNavController().navigate(R.id.action_newPasswordFragment_to_initialFragment)
					}
					restApiStatus.LOADING -> {
						loadingLayout.visibility = View.VISIBLE
					}
					restApiStatus.ERROR -> {
						Toast.makeText(requireContext(),getString(R.string.try_again_msg), Toast.LENGTH_SHORT).show()
						loadingLayout.visibility = View.GONE
					}
					restApiStatus.NOTHING -> { }
				}
			}
			newPasswordText.addTextChangedListener{
				if(it.toString().isBlank()){
					newPassword.error = getString(R.string.empty_field_error_msg)
				}else{
					if(newPasswordConfirmText.text.toString().isNotBlank() && it.toString() != newPasswordConfirmText.text.toString()) {
						newPasswordConfirm.error = getString(R.string.passwords_unmatch_msg)
					}
					if(it.toString() == newPasswordConfirmText.text.toString()){
						newPasswordConfirm.error = null
					}
					newPassword.error = null
				}
			}
			newPasswordConfirmText.addTextChangedListener{
				if(it.toString().isBlank() || ( newPasswordText.text.toString().isNotBlank() && it.toString() != newPasswordText.text.toString())){
					newPasswordConfirm.error = getString(R.string.passwords_unmatch_msg)
				}else if(it.toString() == newPasswordText.text.toString()){
					newPasswordConfirm.error = null
				}
			}
			changeButton.setOnClickListener{
				if(newPassword.error == null && newPasswordConfirm.error == null){
					sharedViewModel.updatePassword(newPasswordText.text.toString().trim())
				}
			}
		}
	}
}
