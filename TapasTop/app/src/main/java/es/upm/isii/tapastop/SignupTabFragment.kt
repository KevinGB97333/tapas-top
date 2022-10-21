package es.upm.isii.tapastop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import es.upm.isii.tapastop.databinding.FragmentSignupTabBinding

class SignupTabFragment : Fragment() {

    private var _binding : FragmentSignupTabBinding?=null

    private val binding get() = _binding!!

    private lateinit var username : TextInputLayout
    private lateinit var email : TextInputLayout
    private lateinit var password : TextInputLayout
    private lateinit var confirmPassword : TextInputLayout
    private lateinit var signUpBtn : Button
    private val alpha =  0F
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupTabBinding.inflate(inflater,container,false)
        val root = binding.root
        username = binding.signupUsername
        email = binding.signupEmail
        password = binding.signupPassword
        confirmPassword = binding.signupConfirmPassword
        signUpBtn = binding.btnSignup

        return root
    }
}