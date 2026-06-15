package com.example.todoapp

import android.content.Context
import java.io.File

class SettingsManager(private val context: Context) {

    private val tasksFile: File
        get() = File(context.filesDir, "tasks.txt")

    // Сохраняем список задач
    fun saveTasks(tasks: List<Task>) {
        val lines = tasks.joinToString("\n") { "${it.id},${it.text},${it.isDone}" }
        tasksFile.writeText(lines)
    }

    // Загружаем список задач
    fun loadTasks(): List<Task> {
        if (!tasksFile.exists()) return emptyList()

        val lines = tasksFile.readLines()
        return lines.mapNotNull { line ->
            val parts = line.split(",")
            if (parts.size == 3) {
                try {
                    Task(parts[0].toInt(), parts[1], parts[2].toBoolean())
                } catch (e: Exception) {
                    null
                }
            } else null
        }
    }
}