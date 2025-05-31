package com.example.loginpage.task

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginpage.R

class TaskListActivity : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskRepository: TaskRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)

        name = intent.getStringExtra("name")!!
        findViewById<TextView>(R.id.txtLoggedUser).text = "Usuario: $name"

        recyclerView = findViewById(R.id.rvTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        taskRepository = TaskRepository(this)

        val tasks = taskRepository.findAll()
        val btnAddTask = findViewById<Button>(R.id.btnAddTask)
        btnAddTask.setOnClickListener {
            showAddTaskDialog()
        }

        taskAdapter = TaskAdapter(
            tasks.toMutableList(),
            onCompletedChanged = { task, isChecked ->
                task.completed = isChecked
                taskRepository.update(task)
            },
            onDeleteClicked = { task ->
                taskRepository.delete(task)
                refreshTasks()
            }
        )

        recyclerView.adapter = taskAdapter
    }

    private fun refreshTasks() {
        val updatedTasks = taskRepository.findAll()
        taskAdapter.updateTasks(updatedTasks)
    }

    private fun showAddTaskDialog() {
        val inputTitle = EditText(this).apply { hint = "Título" }
        val inputDescription = EditText(this).apply { hint = "Descripción" }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 16, 32, 16)
            addView(inputTitle)
            addView(inputDescription)
        }

        AlertDialog.Builder(this)
            .setTitle("Nueva tarea")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val title = inputTitle.text.toString().trim()
                val desc = inputDescription.text.toString().trim()
                if (title.isNotEmpty() && desc.isNotEmpty()) {
                    taskRepository.insert(title, desc)
                    refreshTasks()
                } else
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}