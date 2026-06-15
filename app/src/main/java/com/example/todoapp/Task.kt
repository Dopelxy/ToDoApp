package com.example.todoapp


data class Task(
    val id: Int,           // уникальный ID
    val text: String,      // текст задачи
    var isDone: Boolean = false  // выполнена или нет
)
