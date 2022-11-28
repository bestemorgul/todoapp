package com.bestemorgul

import android.app.Application
import com.bestemorgul.database.ToDoDatabase

class ToDoApplication : Application() {
    val database: ToDoDatabase by lazy { ToDoDatabase.getDatabase(this) }
}