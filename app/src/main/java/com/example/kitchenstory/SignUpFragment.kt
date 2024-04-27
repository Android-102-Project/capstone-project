package com.example.kitchenstory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.kitchenstory.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private lateinit var auth:FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding:FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }
    private fun init(view: View){
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }
    private fun registerEvents(){
        binding.authTextView.setOnClickListener {
            navControl.navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.nextBtn.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()
            val verifyPassword = binding.rePasswordEt.text.toString().trim()

            if(email.isNotEmpty() && password.isNotEmpty() && verifyPassword.isNotEmpty()){
                if(password == verifyPassword){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show()
                            navControl.navigate(R.id.action_signUpFragment_to_homeFragment)
                        } else {
                            Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                            Log.e("SignInFragment", "Login Failed"+ it.exception.toString())
                        }
                    }
                }
            }
        }
    }
}