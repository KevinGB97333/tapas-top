package es.upm.isii.tapastop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import es.upm.isii.tapastop.databinding.LoginTabFragmentBinding

class LoginTabFragment : Fragment() {

    private var _binding : LoginTabFragmentBinding?=null

    private val binding get() = _binding!!

    private lateinit var username : TextInputLayout
    private lateinit var password : TextInputLayout
    private lateinit var forgotPassword : TextView
    private lateinit var loginBtn : Button
    private val alpha =  0F
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginTabFragmentBinding.inflate(inflater,container,false)
        val root = binding.root
        username = binding.loginUsername
        password = binding.loginPassword
        forgotPassword = binding.forgotPassword
        loginBtn = binding.btnLogin

        //Animations
        username.translationX = 800F
        password.translationX = 800F
        forgotPassword.translationX = 800F
        loginBtn.translationX = 800F

        username.alpha = alpha
        password.alpha = alpha
        forgotPassword.alpha = alpha
        loginBtn.alpha = alpha

        username.animate()
            .translationX(0F)
            .alpha(1F)
            .setDuration(800)
            .setStartDelay(300)
            .start()
        password.animate()
            .translationX(0F)
            .alpha(1F)
            .setDuration(800)
            .setStartDelay(500)
            .start()
        forgotPassword.animate()
            .translationX(0F)
            .alpha(1F)
            .setDuration(800)
            .setStartDelay(500)
            .start()
        loginBtn.animate()
            .translationX(0F)
            .alpha(1F)
            .setDuration(800)
            .setStartDelay(700)
            .start()
        return root
    }
}