package com.bestemorgul.todoapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bestemorgul.ToDoApplication
import com.bestemorgul.todoapp.databinding.FragmentToDoListBinding
import com.bestemorgul.todoapp.ui.adapter.ToDoListAdapter
import com.bestemorgul.todoapp.ui.viewmodel.ToDoViewModel
import com.bestemorgul.todoapp.ui.viewmodel.ToDoViewModelFactory


class ToDoListFragment : Fragment() {

    private var _binding: FragmentToDoListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ToDoViewModel by activityViewModels {
        ToDoViewModelFactory(
            (activity?.application as ToDoApplication).database.todoDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentToDoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            val adapter = ToDoListAdapter { todo ->
            val action = ToDoListFragmentDirections.actionTododetail(todo.id)
            findNavController().navigate(action)
        }

        viewModel.todoList.observe(this.viewLifecycleOwner) {
                adapter.submitList(it)

            binding.emptyListTv.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
        }


          binding.apply {
              rvTodo.adapter = adapter
              fabAdd.setOnClickListener {
                    val action = ToDoListFragmentDirections.actionToCreateTodo()
                    Navigation.findNavController(it).navigate(action)

                }
            }


        }
    }

