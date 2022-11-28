package com.bestemorgul.todoapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bestemorgul.todoapp.databinding.TodoItemDesignBinding
import com.bestemorgul.todoapp.model.ToDoModel


class ToDoListAdapter (private val clickListener: (ToDoModel) -> Unit
) : ListAdapter<ToDoModel, ToDoListAdapter.TodoViewHolder>(DiffCallback) {

    class TodoViewHolder(
        private var binding: TodoItemDesignBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: ToDoModel) {
            binding.tvTodo.text = todo.title

        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<ToDoModel>() {
        override fun areItemsTheSame(oldItem: ToDoModel, newItem: ToDoModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ToDoModel, newItem: ToDoModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TodoViewHolder(
            TodoItemDesignBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = getItem(position)
        holder.itemView.setOnClickListener{
            clickListener(todo)
        }
        holder.bind(todo)
    }
}