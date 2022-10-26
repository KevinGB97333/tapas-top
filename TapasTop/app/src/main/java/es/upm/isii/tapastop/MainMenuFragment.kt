package es.upm.isii.tapastop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import es.upm.isii.tapastop.databinding.FragmentMainMenuBinding
import es.upm.isii.tapastop.model.UserViewModel


class MainMenuFragment : Fragment() {

    // Binding object instance corresponding to the fragment_main_menu.xml layout
    private var _binding : FragmentMainMenuBinding?=null
    private val binding get()=_binding!!

    // ViewModel object instance corresponding to the UserViewModel.kt class
    private val shareViewModel : UserViewModel by activityViewModels()

    private lateinit var viewProfileBtn : Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainMenuBinding.inflate(inflater,container,false)
        val root = binding.root
        viewProfileBtn = binding.viewProfileBtn
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewProfileBtn.setOnClickListener{
                findNavController().navigate(R.id.action_mainMenuFragment_to_profileFragment)
            }
            viewModel = shareViewModel
        }
    }
}