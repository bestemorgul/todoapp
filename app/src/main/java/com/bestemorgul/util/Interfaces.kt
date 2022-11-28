package com.bestemorgul.todoapp.ui

import android.view.View

interface ButtonAddTodoClickListener {
    fun onButtonAddTodo (view:View)
}

interface DateClickListener {
    fun onDateClick(view: View)
}

interface TimeClickListener {
    fun onTimeClick(view: View)
}
