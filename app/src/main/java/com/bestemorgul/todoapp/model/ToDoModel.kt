package com.bestemorgul.todoapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "todomodel")
data class ToDoModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name= "title")
    var title:String,
    @ColumnInfo(name="todo")
    var todo:String,
    @ColumnInfo(name= "todo_date")
    var todo_date:Int
)
