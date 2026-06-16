package com.example.todoapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var rvTasks: RecyclerView
    private lateinit var btnAddTask: MaterialButton
    private lateinit var adapter: TaskAdapter
    private val tasks = mutableListOf<Task>()
    private var nextId = 1
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvTasks = findViewById(R.id.rvTasks)
        btnAddTask = findViewById(R.id.btnAddTask)

        settingsManager = SettingsManager(this)

        loadTasks()

        adapter = TaskAdapter(
            tasks = tasks,
            onDeleteClick = { task ->
                tasks.remove(task)
                saveTasks()
                adapter.updateList(tasks.toList())
            },
            onDoneClick = { task, isChecked ->
                task.isDone = isChecked
                saveTasks()
            }
        )

        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = adapter

        btnAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_task, null)
        val dialog = android.app.Dialog(this)
        dialog.setContentView(dialogView)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val etTaskTitle = dialogView.findViewById<EditText>(R.id.etTaskTitle)
        val btnAdd = dialogView.findViewById<Button>(R.id.btnAdd)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        btnAdd.setOnClickListener {
            val taskText = etTaskTitle.text.toString().trim()
            if (taskText.isNotEmpty()) {
                val newTask = Task(nextId++, taskText, false)
                tasks.add(newTask)
                saveTasks()
                adapter.updateList(tasks.toList())
                dialog.dismiss()
            } else {
                etTaskTitle.error = "Введите название задачи"
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun loadTasks() {
        val loadedTasks = settingsManager.loadTasks()
        tasks.clear()
        tasks.addAll(loadedTasks)
        if (tasks.isNotEmpty()) {
            nextId = tasks.maxOfOrNull { it.id }?.plus(1) ?: 1
        }
    }

    private fun saveTasks() {
        settingsManager.saveTasks(tasks)
    }
}