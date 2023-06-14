package com.example.todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todo.R
import androidx.navigation.findNavController
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.utils.ToDoAdapter
import com.example.todo.utils.ToDoAdapterClickListeners
import com.example.todo.utils.ToDoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment(), TaskPopUpClickListener, ToDoAdapterClickListeners {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private var addToDoPopUpFragment: AddToDoPopUpFragment? = null
    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var tasks: MutableList<ToDoData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        getDataFromFirebase()
        registerEvents()
    }

    private fun init(view: View) {
        navController = view.findNavController()
        firebaseAuth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Tasks")
            .child(firebaseAuth.currentUser?.uid.toString())

        tasks = mutableListOf<ToDoData>()

        toDoAdapter = ToDoAdapter()
        binding.tasksRv.adapter = toDoAdapter
        toDoAdapter.submitList(tasks)
        toDoAdapter.setToDoClickListeners(this)

    }

    private fun getDataFromFirebase() {

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tasks.clear()

                for (taskSnapshot in snapshot.children) {
                    val toDoTask = taskSnapshot.key?.let {
                        ToDoData(it, taskSnapshot.value.toString())
                    }
                    if (toDoTask != null) {
                        tasks.add(toDoTask)
                    }
                }
                toDoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun registerEvents() {

        binding.signOutBtn.setOnClickListener {
            firebaseAuth.signOut()
            navController.navigate(R.id.action_homeFragment_to_signInFragment)

        }

        binding.addSomethingBtn.setOnClickListener {

            if (addToDoPopUpFragment != null)
                childFragmentManager.beginTransaction().remove(addToDoPopUpFragment!!).commit()

            addToDoPopUpFragment = AddToDoPopUpFragment()
            addToDoPopUpFragment!!.setAddTaskClickListener(this)
            addToDoPopUpFragment!!.show(childFragmentManager, AddToDoPopUpFragment.TAG)
        }


    }

    override fun saveTask(task: String, toDoEt: TextInputEditText) {

        databaseRef.push().setValue(task).addOnCompleteListener {

            if (it.isSuccessful) {
                Toast.makeText(context, "Task Saved Successfully!", Toast.LENGTH_SHORT).show()
                toDoEt.text = null

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }

            addToDoPopUpFragment!!.dismiss()
        }

    }

    override fun updateTask(changedToDo: ToDoData, toDoEt: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[changedToDo.taskID] = changedToDo.task
        databaseRef.updateChildren(map).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()

            }
            toDoEt.text = null
            addToDoPopUpFragment!!.dismiss()

        }
    }

    override fun editTaskListener(toDoData: ToDoData) {
        if(addToDoPopUpFragment!= null)
            childFragmentManager.beginTransaction().remove(addToDoPopUpFragment!!).commit()

        addToDoPopUpFragment = AddToDoPopUpFragment.newInstance(toDoData.taskID, toDoData.task)
        addToDoPopUpFragment!!.setAddTaskClickListener(this)
        addToDoPopUpFragment!!.show(childFragmentManager, AddToDoPopUpFragment.TAG)
    }

    override fun deleteTaskListener(toDoData: ToDoData) {
        databaseRef.child(toDoData.taskID).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}