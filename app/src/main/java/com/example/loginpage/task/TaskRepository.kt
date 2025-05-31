package com.example.loginpage.task

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskRepository(context: Context) : SQLiteOpenHelper(context, "tasks.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                completed INTEGER NOT NULL DEFAULT 0
            );
        """.trimIndent()
        db?.execSQL(createTable)

        // Initial data
        insertInitialTask(db, "Task 1", "Task 1 description")
        insertInitialTask(db, "Task 2", "Task 2 description")
        insertInitialTask(db, "Task 3", "Task 3 description")
        insertInitialTask(db, "Task 4", "Task 4 description")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    fun findAll(): List<Task> {
        val db = this.readableDatabase
        val query = "SELECT * FROM tasks"
        val cursor = db.rawQuery(query, null)
        val tasks = mutableListOf<Task>()

        while (cursor.moveToNext()) {
            val task = Task(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                completed = cursor.getInt(cursor.getColumnIndexOrThrow("completed")) == 1
            )
            tasks.add(task)
        }

        cursor.close()
        return tasks
    }

    fun findById(id: Int): Task? {
        val db = this.readableDatabase
        val query = "SELECT * FROM tasks WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        val task = if (cursor.moveToFirst()) {
            Task(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                completed = cursor.getInt(cursor.getColumnIndexOrThrow("completed")) == 1
            )
        } else null

        cursor.close()
        return task
    }

    fun insert(title: String, description: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("description", description)
        }
        return db.insert("tasks", null, values)
    }

    fun update(task: Task): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", task.title)
            put("description", task.description)
            put("completed", task.completed)
        }
        return db.update("tasks", values, "id = ?", arrayOf(task.id.toString()))
    }

    fun delete(task: Task): Int {
        val db = this.writableDatabase
        return db.delete("tasks", "id = ?", arrayOf(task.id.toString()))
    }

    private fun insertInitialTask(db: SQLiteDatabase?, title: String, description: String) {
        val values = ContentValues().apply {
            put("title", title)
            put("description", description)
        }
        db?.insert("tasks", null, values)
    }
}