package com.example.todo.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.TodoListBinding

class ToDoAdapter() :
    ListAdapter<ToDoData, ToDoAdapter.ToDoViewHolder>(ToDoDifference()) {

    private var toDoAdapterClickListeners: ToDoAdapterClickListeners? = null

    fun setToDoClickListeners(listeners: ToDoAdapterClickListeners) {
        this.toDoAdapterClickListeners = listeners
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder.createToDo(parent)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bindToDo(getItem(position), toDoAdapterClickListeners)
    }

    class ToDoViewHolder private constructor(private val binding: TodoListBinding) :
        RecyclerView.ViewHolder(binding.root) {


        companion object {

            fun createToDo(parent: ViewGroup): ToDoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TodoListBinding.inflate(layoutInflater, parent, false)
                return ToDoViewHolder(binding)
            }
        }

        fun bindToDo(item: ToDoData, toDoAdapterClickListeners: ToDoAdapterClickListeners?) {
            binding.taskTw.text = item.task

            binding.editTodoIv.setOnClickListener {
                toDoAdapterClickListeners?.editTaskListener(item)
            }

            binding.deleteTodoIv.setOnClickListener {
                toDoAdapterClickListeners?.deleteTaskListener(item)
            }

            binding.executePendingBindings()

        }

    }
}

class ToDoDifference : DiffUtil.ItemCallback<ToDoData>() {

    override fun areItemsTheSame(oldItem: ToDoData, newItem: ToDoData): Boolean {
        return oldItem.taskID == newItem.taskID
    }

    override fun areContentsTheSame(oldItem: ToDoData, newItem: ToDoData): Boolean {
        return oldItem == newItem
    }
}

interface ToDoAdapterClickListeners {

    fun editTaskListener(toDoData: ToDoData)

    fun deleteTaskListener(toDoData: ToDoData)
}