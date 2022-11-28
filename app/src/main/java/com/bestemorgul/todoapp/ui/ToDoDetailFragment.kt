package com.bestemorgul.todoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bestemorgul.ToDoApplication
import com.bestemorgul.todoapp.R
import com.bestemorgul.todoapp.databinding.FragmentToDoDetailBinding
import com.bestemorgul.todoapp.model.ToDoModel
import com.bestemorgul.todoapp.ui.viewmodel.ToDoViewModel
import com.bestemorgul.todoapp.ui.viewmodel.ToDoViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ToDoDetailFragment : Fragment() {

    private var _binding: FragmentToDoDetailBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: ToDoDetailFragmentArgs by navArgs()


    private val viewModel: ToDoViewModel by activityViewModels {
        ToDoViewModelFactory(
            (activity?.application as ToDoApplication).database.todoDao()
        )
    }

    private lateinit var todo: ToDoModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentToDoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (id > 0) {

            val id = navigationArgs.id
            viewModel.selectTodo(id).observe(this.viewLifecycleOwner) { selectedTodo ->
                todo = selectedTodo
                bindTodo()

            }
            binding.actionDelete.visibility = View.VISIBLE
            binding.actionDelete.setOnClickListener {
                deleteTodo()
            }
        }
    }


    private fun bindTodo() {
        binding.apply {
            titleDetail.text= todo.title
            todoDetail.text = todo.todo

            actionDelete.setOnClickListener {showConfirmationDialog()}

            actionEdit.setOnClickListener {
                val action = ToDoDetailFragmentDirections.actionToDoDetailFragmentToCreateToDoFragment(todo.id)
                findNavController().navigate(action)
            }
        }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.answer_no)) { _, _ -> }
            .setPositiveButton(getString(R.string.answer_yes)) { _, _ ->
                deleteTodo()
            }
            .show()
    }


    private fun deleteTodo() {
        viewModel.deleteTodo(todo)
        findNavController().navigateUp()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
