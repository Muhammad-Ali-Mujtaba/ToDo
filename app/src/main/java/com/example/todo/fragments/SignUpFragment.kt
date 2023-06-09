package com.example.todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.todo.R
import com.example.todo.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()

    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun registerEvents() {

        binding.signInInsteadTw.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.signUpDoneBtn.setOnClickListener {

            val email = binding.emailEditText.text.toString().trim()
            val password = binding.createPasswordEditText.text.toString().trim()
            val confirmPass = binding.confirmPasswordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (password == confirmPass) {

                    binding.progressBar.visibility = View.VISIBLE

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT)
                                    .show()
                                navController.navigate(R.id.action_signUpFragment_to_homeFragment)

                            } else {
                                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                            binding.progressBar.visibility = View.GONE

                        }


                } else {
                    Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT)
                        .show()
                    binding.progressBar.visibility = View.GONE

                }

            }else{
                Toast.makeText(context, "Empty fields not allowed", Toast.LENGTH_SHORT).show()
            }

        }
    }

}