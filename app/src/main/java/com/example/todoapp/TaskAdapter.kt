package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: List<Task>,
    private val onDeleteClick: (Task) -> Unit,
    private val onDoneClick: (Task, Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun updateList(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cbDone: CheckBox = itemView.findViewById(R.id.cbDone)
        private val tvTaskText: TextView = itemView.findViewById(R.id.tvTaskText)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(task: Task) {
            tvTaskText.text = task.text
            cbDone.isChecked = task.isDone

            // Обновляем зачёркивание
            if (task.isDone) {
                tvTaskText.paintFlags = tvTaskText.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvTaskText.paintFlags = tvTaskText.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            cbDone.setOnCheckedChangeListener(null)
            cbDone.setOnCheckedChangeListener { _, isChecked ->
                task.isDone = isChecked
                onDoneClick(task, isChecked)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(task)
            }
        }
    }
}