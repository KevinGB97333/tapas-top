package es.upm.isii.tapastop

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import es.upm.isii.tapastop.databinding.FragmentPasswordRecoveryBinding
import es.upm.isii.tapastop.model.UserViewModel

class PasswordRecoveryFragment : Fragment() {
	private var _binding : FragmentPasswordRecoveryBinding ?= null
	private val binding get() = _binding!!

	private val sharedViewModel : UserViewModel by activityViewModels()

	private lateinit var editTextOne: EditText
	private lateinit var editTextTwo: EditText
	private lateinit var editTextThree: EditText
	private lateinit var editTextFour: EditText
	private lateinit var frameLayoutRoot: FrameLayout
	private lateinit var codeLayout: LinearLayout

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentPasswordRecoveryBinding.inflate(inflater,container,false)
		val root = binding.root
		frameLayoutRoot = binding.frameLayoutRoot
		editTextOne = binding.editTextOne
		editTextTwo = binding.editTextTwo
		editTextThree = binding.editTextThree
		editTextFour = binding.editTextFour
		codeLayout = binding.codeLayout
		setListener()
		initFocus()
		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.apply {
			resendCodeTextview.setOnClickListener {
				sharedViewModel.sendRecoveryEmail(sharedViewModel.usernameRecovery)
			}
		}
	}

	private fun setListener() {

		frameLayoutRoot.setOnClickListener {
			val inputManager: InputMethodManager =
				requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			inputManager.hideSoftInputFromWindow(frameLayoutRoot.windowToken, 0)
		}

		setTextChangeListener(fromEditText = editTextOne, targetEditText = editTextTwo)
		setTextChangeListener(fromEditText = editTextTwo, targetEditText = editTextThree)
		setTextChangeListener(fromEditText = editTextThree, targetEditText = editTextFour)
		setTextChangeListener(fromEditText = editTextFour, done = {
			if (verifyCode()) {
				findNavController().navigate(R.id.action_passwordRecoveryFragment_to_newPasswordFragment)
			}
		})
		setKeyListener(fromEditText = editTextTwo, backToEditText = editTextOne)
		setKeyListener(fromEditText = editTextThree, backToEditText = editTextTwo)
		setKeyListener(fromEditText = editTextFour, backToEditText = editTextThree)
	}

	private fun initFocus() {
		editTextOne.isEnabled = true

		editTextOne.postDelayed({
			editTextOne.requestFocus()
			val inputMethodManager =
				requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			inputMethodManager.showSoftInput(editTextOne, InputMethodManager.SHOW_FORCED)
		}, 500)
	}

	private fun reset() {
		editTextOne.isEnabled = false
		editTextTwo.isEnabled = false
		editTextThree.isEnabled = false
		editTextFour.isEnabled = false

		editTextOne.setText("")
		editTextTwo.setText("")
		editTextThree.setText("")
		editTextFour.setText("")
		initFocus()
	}

	private fun setTextChangeListener(
		fromEditText: EditText,
		targetEditText: EditText? = null,
		done: (() -> Unit)? = null
	) {
		fromEditText.addTextChangedListener {
			it?.let { string ->
				if (string.isNotEmpty()) {
					targetEditText?.let { editText ->
						editText.isEnabled = true
						editText.requestFocus()
					} ?: run {
						done?.let { done ->
							done()
						}
					}
					fromEditText.clearFocus()
					fromEditText.isEnabled = false
				}
			}
		}
	}

	private fun setKeyListener(fromEditText: EditText, backToEditText: EditText) {
		fromEditText.setOnKeyListener { _, _, event ->

			if (null != event && KeyEvent.KEYCODE_DEL == event.keyCode && KeyEvent.ACTION_DOWN == event.action) {
				backToEditText.isEnabled = true
				backToEditText.requestFocus()
				backToEditText.setText("")

				fromEditText.clearFocus()
				fromEditText.isEnabled = false
			}
			false
		}
	}

	private fun verifyCode(): Boolean {
		var success = false
		val code = "${editTextOne.text.toString().trim()}" +
				"${editTextTwo.text.toString().trim()}" +
				"${editTextThree.text.toString().trim()}" +
				"${editTextFour.text.toString().trim()}"

		if (code.length == 4 && code == sharedViewModel.code) {
			Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
			val inputManager =
				requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			inputManager.hideSoftInputFromWindow(frameLayoutRoot.windowToken, 0)
			success = true
		}
		if (!success) {
			Toast.makeText(requireContext(), "Wrong Code", Toast.LENGTH_SHORT).show()
			reset()
		}
		return success
	}
}