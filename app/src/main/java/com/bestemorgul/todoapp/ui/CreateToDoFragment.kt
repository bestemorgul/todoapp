package com.bestemorgul.todoapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.bestemorgul.ToDoApplication
import com.bestemorgul.todoapp.R
import com.bestemorgul.todoapp.databinding.FragmentCreateToDoBinding
import com.bestemorgul.todoapp.model.ToDoModel
import com.bestemorgul.todoapp.ui.viewmodel.ToDoViewModel
import com.bestemorgul.todoapp.ui.viewmodel.ToDoViewModelFactory
import com.bestemorgul.util.TodoWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit


class CreateToDoFragment : Fragment(), ButtonAddTodoClickListener,
                                       DateClickListener,
                                       TimeClickListener,
                   DatePickerDialog.OnDateSetListener,
                   TimePickerDialog.OnTimeSetListener{

    private var _binding: FragmentCreateToDoBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: CreateToDoFragmentArgs by navArgs()
    private lateinit var todo: ToDoModel
    var year= 0
    var month = 0
    var day= 0
    var hour= 0
    var minute= 0


    private val viewModel: ToDoViewModel by activityViewModels {
        ToDoViewModelFactory(
            (activity?.application as ToDoApplication).database.todoDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateToDoBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.actionDelete.visibility = View.INVISIBLE

        binding.todo = ToDoModel(title = "", todo = "", todo_date = 0)
        binding.listener = this
        binding.listenerDate= this
        binding.listenerTime= this


        val id = navigationArgs.id
        if (id > 0) {

            viewModel.selectTodo(id).observe(this.viewLifecycleOwner) { selectedTodo ->
                todo= selectedTodo
                bindTodo(todo)
            }

            binding.actionDelete.visibility = View.VISIBLE
            binding.actionDelete.setOnClickListener {
                deleteTodo(todo)
            }


        } else {

            binding.actionDelete.visibility = View.INVISIBLE

        }
    }


    private fun updateTodo() {
        if (isValidEntry()) {
            viewModel.updateTodo(
                id = navigationArgs.id,
                title = binding.titleText.text.toString(),
                todo = binding.todoText.text.toString(),
                todo_date = 0
            )
            findNavController().navigate(
                R.id.action_createToDoFragment_to_toDoListFragment
            )
        }
    }

    private fun bindTodo(todo: ToDoModel) {

        binding.apply{
            titleText.setText(todo.title, TextView.BufferType.SPANNABLE)
            todoText.setText(todo.todo, TextView.BufferType.SPANNABLE)

            actionCreate.setOnClickListener {
                updateTodo()
            }
        }

    }

    private fun isValidEntry () : Boolean {
        return viewModel.isValidEntry(
            binding.titleText.text.toString(),
            binding.todoText.text.toString()
        )
    }

    private fun addTodo() {
        if (isValidEntry()) {
            viewModel.addToDo(
                binding.titleText.text.toString(),
                binding.todoText.text.toString(),
                todo_date = 0

            )
            findNavController().navigate(
                R.id.action_createToDoFragment_to_toDoListFragment
            )
        }
    }

    private fun deleteTodo(todo: ToDoModel) {
        viewModel.deleteTodo(todo)
        findNavController().navigate(
            R.id.action_createToDoFragment_to_toDoListFragment
        )
    }



    override fun onDestroyView() {
        super.onDestroyView()

        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    override fun onButtonAddTodo(view: View) {
        val c= Calendar.getInstance()
        c.set(year, month, day, hour, minute)

        val today = Calendar.getInstance()
        val diff = (c.timeInMillis/1000L)-(today.timeInMillis/1000L)

        binding.todo!!.todo_date = (c.timeInMillis/1000L).toInt()

        binding.actionCreate.setOnClickListener {
            addTodo()

            val myWorkRequest = OneTimeWorkRequestBuilder<TodoWorker>()
                .setInitialDelay(diff, TimeUnit.SECONDS)
                .setInputData(
                    workDataOf(
                        "title" to binding.titleText.text.toString(),
                        "message" to binding.todoText.text.toString()
                    )
                ).build()
            WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)
        }

    }

    override fun onDateClick(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        activity?.let { DatePickerDialog(it,this, year, month, day).show() }
    }

    override fun onTimeClick(view: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        TimePickerDialog(activity, this, hour, minute, android.text.format.DateFormat.is24HourFormat(activity)).show()

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
     Calendar.getInstance().let {
         it.set(year, month, day)
         // dd-mm-yyyy
         // 01-05-2021
         binding.todoDate.setText(day.toString().padStart(2,'0') + "-"
                 + (month+1).toString().padStart(2, '0') + "-" + year)
         this.year = year
         this.month = month
         this.day = day
     }
    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        //HH:MM
     binding.todoTime.setText(hourOfDay.toString().padStart(2,'0') + ":"
     +minute.toString().padStart(2,'0'))

        this.hour= hourOfDay
        this.minute = minute
    }
}



