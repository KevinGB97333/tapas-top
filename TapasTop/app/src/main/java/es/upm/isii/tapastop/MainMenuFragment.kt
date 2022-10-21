package es.upm.isii.tapastop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import es.upm.isii.tapastop.databinding.FragmentMainMenuBinding


class MainMenuFragment : Fragment() {
    private var _binding : FragmentMainMenuBinding?=null

    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainMenuBinding.inflate(inflater,container,false)
        val root = binding.root

        return root
    }

}