package com.example.todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todo.R
import com.example.todo.databinding.FragmentAddToDoPopUpBinding
import com.google.android.material.textfield.TextInputEditText

class AddToDoPopUpFragment : DialogFragment() {

    private lateinit var binding: FragmentAddToDoPopUpBinding
    private lateinit var addTaskClickListener: AddTaskClickListener

    fun setAddTaskClickListener(listener: AddTaskClickListener) {
        addTaskClickListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddToDoPopUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()
    }

    private fun registerEvents() {

        binding.toDoClose.setOnClickListener {
            dismiss()
        }

        binding.toDoNextBtn.setOnClickListener {

            val toDoTask = binding.toDoEt.text.toString()

            if (toDoTask.isNotEmpty()) {
                addTaskClickListener.saveTask(toDoTask, binding.toDoEt)

            } else {
                Toast.makeText(context, "Please enter a task", Toast.LENGTH_SHORT)
            }

        }
    }

}

interface AddTaskClickListener {

    fun saveTask(task: String, toDoEt: TextInputEditText)

}