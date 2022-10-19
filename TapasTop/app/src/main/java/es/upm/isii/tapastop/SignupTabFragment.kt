package es.upm.isii.tapastop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import es.upm.isii.tapastop.databinding.SignupTabFragmentBinding

class SignupTabFragment : Fragment() {

    private var _binding : SignupTabFragmentBinding?=null

    private val binding get() = _binding!!

    private lateinit var username : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var confirmPassword : EditText
    private lateinit var signUpBtn : Button
    private val alpha =  0F
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignupTabFragmentBinding.inflate(inflater,container,false)
        val root = binding.root
        username = binding.signupUsername
        email = binding.signupEmail
        password = binding.signupPassword
        confirmPassword = binding.signupConfirmPassword
        signUpBtn = binding.btnSignup

        return root
    }
}