package com.example.loginpage.task

data class Task(
    val id: Int,
    val title: String,
    val description: String?,
    var completed: Boolean
)
