package com.example.loginpage.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginpage.R

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val onCompletedChanged: (Task, Boolean) -> Unit,
    private val onDeleteClicked: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chkCompleted: CheckBox = view.findViewById(R.id.chkCompleted)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtDescription: TextView = view.findViewById(R.id.txtDescription)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.txtTitle.text = task.title
        holder.txtDescription.text = task.description
        holder.chkCompleted.isChecked = task.completed

        holder.chkCompleted.setOnCheckedChangeListener(null)
        holder.chkCompleted.setOnCheckedChangeListener { _, isChecked ->
            onCompletedChanged(task, isChecked)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClicked(task)
        }
    }

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}