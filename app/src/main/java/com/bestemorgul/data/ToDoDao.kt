package com.bestemorgul.data

import androidx.room.*
import com.bestemorgul.todoapp.model.ToDoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(todo: ToDoModel)

    @Query("SELECT * FROM todomodel")
     fun getTodo(): Flow<List<ToDoModel>>

    @Query("SELECT * FROM todomodel WHERE id = :id")
    fun selectTodo(id:Int) : Flow<ToDoModel>

    @Update
    fun update(todo: ToDoModel)

    @Delete
     fun deleteTodo(todo:ToDoModel)




}