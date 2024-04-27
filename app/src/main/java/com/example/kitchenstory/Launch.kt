package com.example.kitchenstory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.kitchenstory.databinding.FragmentLaunchBinding
import com.google.firebase.auth.FirebaseAuth

class Launch : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentLaunchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()

        view.findViewById<View>(R.id.btnStartCooking).setOnClickListener {
            navigateToDestination()
        }
    }

    private fun navigateToDestination() {
        if (auth.currentUser != null) {
            // User is already authenticated, navigate to home fragment
            navControl.navigate(R.id.action_launch_to_homeFragment)
        } else {
            // User is not authenticated, navigate to sign-in fragment
            navControl.navigate(R.id.action_launch_to_signInFragment)
        }
    }

}