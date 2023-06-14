package com.example.todo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todo.databinding.FragmentAddToDoPopUpBinding
import com.example.todo.utils.ToDoData
import com.google.android.material.textfield.TextInputEditText

class AddToDoPopUpFragment : DialogFragment() {

    private lateinit var binding: FragmentAddToDoPopUpBinding
    private lateinit var taskPopUpClickListener: TaskPopUpClickListener
    private var currentToDo: ToDoData? = null

    companion object {

        const val TAG = "AddToDoPopUpFragment"

        fun newInstance(taskID: String, task: String) = AddToDoPopUpFragment().apply {
            arguments = Bundle().apply {
                putString("taskID", taskID)
                putString("task", task)
            }
        }
    }

    fun setAddTaskClickListener(listener: TaskPopUpClickListener) {
        taskPopUpClickListener = listener
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

        if (arguments != null) {
            currentToDo = ToDoData(
                arguments?.getString("taskID").toString(),
                arguments?.getString("task").toString()
            )

            binding.toDoEt.setText(currentToDo?.task)
        }
        registerEvents()
    }

    private fun registerEvents() {

        binding.toDoClose.setOnClickListener {
            dismiss()
        }

        binding.toDoNextBtn.setOnClickListener {

            val toDoTask = binding.toDoEt.text.toString()

            if (toDoTask.isNotEmpty()) {
                if (currentToDo == null) {
                    taskPopUpClickListener.saveTask(toDoTask, binding.toDoEt)
                } else {
                    currentToDo?.task = toDoTask
                    taskPopUpClickListener.updateTask(currentToDo!!, binding.toDoEt)

                }
            } else {
                Toast.makeText(context, "Please enter a task", Toast.LENGTH_SHORT)
            }

        }
    }

}

interface TaskPopUpClickListener {

    fun saveTask(task: String, toDoEt: TextInputEditText)
    fun updateTask(changedToDo: ToDoData, toDoEt: TextInputEditText)

}