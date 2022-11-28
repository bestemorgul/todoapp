package com.bestemorgul.todoapp.ui.viewmodel

import androidx.lifecycle.*
import com.bestemorgul.data.ToDoDao
import com.bestemorgul.todoapp.model.ToDoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData

class ToDoViewModel(private val todoDao: ToDoDao): ViewModel() {

    val todoList: LiveData<List<ToDoModel>> = todoDao.getTodo().asLiveData()

    fun addToDo(
        title: String,
        todo: String,
        todo_date:Int
    ) {
        val todo = ToDoModel(
            title = title,
            todo = todo,
            todo_date = todo_date
        )

        viewModelScope.launch(Dispatchers.IO) {
            todoDao.insert(todo)

        }
    }

    fun selectTodo(id: Int): LiveData<ToDoModel> {
        return todoDao.selectTodo(id).asLiveData()
    }

    fun deleteTodo(todo: ToDoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.deleteTodo(todo)
        }
    }

    fun updateTodo(
        id: Int,
        title: String,
        todo: String,
        todo_date:Int
    ) {
        val todo = ToDoModel(
            id = id,
            title = title,
            todo = todo,
            todo_date = todo_date
        )

        viewModelScope.launch(Dispatchers.IO) {
            todoDao.update(todo)
        }

    }

    fun isValidEntry(title:String, todo: String): Boolean {
        return title.isNotBlank() && todo.isNotBlank()
    }
}

