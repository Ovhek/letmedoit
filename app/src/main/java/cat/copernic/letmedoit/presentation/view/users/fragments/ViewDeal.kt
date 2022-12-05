package cat.copernic.letmedoit.presentation.view.users.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.copernic.letmedoit.databinding.FragmentVerDealBinding

class ViewDeal : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    lateinit var binding: FragmentVerDealBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerDealBinding.inflate(inflater,container,false)
        binding.btnBack.setOnClickListener { requireActivity().onBackPressed() }
        return binding.root
    }

}