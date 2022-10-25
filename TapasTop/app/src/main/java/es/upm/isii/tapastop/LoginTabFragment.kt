package es.upm.isii.tapastop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import es.upm.isii.tapastop.databinding.FragmentLoginTabBinding

class LoginTabFragment : Fragment() {

    private var _binding : FragmentLoginTabBinding?=null

    private val binding get() = _binding!!

    private lateinit var username : TextInputLayout
    private lateinit var password : TextInputLayout
    private lateinit var forgotPassword : TextView
    private lateinit var loginBtn : Button

    private lateinit var usernameAnimation : Animation
    private lateinit var passwordAnimation : Animation
    private lateinit var loginAnimation : Animation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginTabBinding.inflate(inflater,container,false)
        val root = binding.root
        username = binding.loginUsername
        password = binding.loginPassword
        forgotPassword = binding.forgotPassword
        loginBtn = binding.btnLogin

        //Animations
        usernameAnimation = AnimationUtils.loadAnimation(activity,R.anim.start_username_field_anim)
        passwordAnimation = AnimationUtils.loadAnimation(activity, R.anim.start_password_field_anim)
        loginAnimation = AnimationUtils.loadAnimation(activity, R.anim.start_button_anim)

        username.startAnimation(usernameAnimation)
        password.startAnimation(passwordAnimation)
        forgotPassword.startAnimation(passwordAnimation)
        loginBtn.startAnimation(loginAnimation)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            loginBtn.setOnClickListener{
                findNavController().navigate(R.id.action_initialFragment_to_mainMenuFragment)
            }
        }
    }
}