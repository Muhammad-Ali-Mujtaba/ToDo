package com.example.todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.todo.R
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.utils.ToDoAdapter
import com.example.todo.utils.ToDoAdapterClickListeners
import com.example.todo.utils.ToDoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment(), AddTaskClickListener, ToDoAdapterClickListeners {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var addToDoPopUpFragment: AddToDoPopUpFragment
    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var tasks: MutableList<ToDoData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }

    private fun init(view: View) {
        navController = view.findNavController()
        firebaseAuth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Tasks").child(firebaseAuth.currentUser?.uid.toString())

        tasks =
            mutableListOf<ToDoData>(
                ToDoData("1", "Buy Groceries"),
                ToDoData("2", "Study"),
                ToDoData("3", "Complete Assignment"),
                ToDoData("4", "Work"),
                ToDoData("5", "Games"),
                ToDoData("6", "Play"),
                ToDoData("7", "Meditation"),
                ToDoData("8", "Pray"),
                ToDoData("9", "Exercise"),
                ToDoData("10", "Learn German"),
                ToDoData("11", "Android Development"),
                ToDoData("12", "Family Time"),
                ToDoData("13", "Social Time"),
                ToDoData("14", "Go for a walk"),
                ToDoData("15", "Buy a water bottle"),
                ToDoData("16", "Buy Eggs"),
                ToDoData("17", "Clean the kitchen"),
                ToDoData("18", "Job Application"),
                ToDoData("19", "Practice Greatfulness"),
                ToDoData("20", "Call Mom"),
                ToDoData("21", "Sleep on time"),

                )

        toDoAdapter = ToDoAdapter()
        binding.tasksRv.adapter = toDoAdapter
        toDoAdapter.submitList(tasks)
        toDoAdapter.setToDoClickListeners(this)

    }

    private fun registerEvents() {

        binding.addSomethingBtn.setOnClickListener {
            addToDoPopUpFragment = AddToDoPopUpFragment()
            addToDoPopUpFragment.setAddTaskClickListener(this)
            addToDoPopUpFragment.show(childFragmentManager, "AddToDoPopUpFragment")
        }

    }

    override fun saveTask(task: String, toDoEt: TextInputEditText) {

        databaseRef.push().setValue(task).addOnCompleteListener {

            if (it.isSuccessful) {
                Toast.makeText(context, "Task Saved Successfully!", Toast.LENGTH_SHORT)
                toDoEt.text = null

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT)
            }

            addToDoPopUpFragment.dismiss()
        }

    }

    override fun editTaskListener(toDoData: ToDoData) {
        TODO("Not yet implemented")
    }

    override fun deleteTaskListener(toDoData: ToDoData) {
        TODO("Not yet implemented")
    }

}