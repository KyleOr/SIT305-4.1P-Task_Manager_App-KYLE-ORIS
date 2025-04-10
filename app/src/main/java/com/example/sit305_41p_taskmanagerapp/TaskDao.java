package com.example.sit305_41p_taskmanagerapp;

import androidx.room.*;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    // Existing method for fetching tasks without sorting
    @Query("SELECT * FROM Task ORDER BY dueDate ASC")
    List<Task> getAllTasks();

    // New method to get tasks sorted by due date
    @Query("SELECT * FROM Task ORDER BY dueDate ASC")
    List<Task> getAllTasksSortedByDueDate();

    // New method to get tasks sorted by title
    @Query("SELECT * FROM Task WHERE id = :id")
    Task getTaskById(int id);

}
