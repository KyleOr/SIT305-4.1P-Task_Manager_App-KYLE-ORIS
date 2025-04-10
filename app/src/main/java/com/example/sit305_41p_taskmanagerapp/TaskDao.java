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

    @Query("SELECT * FROM Task ORDER BY dueDate ASC")
    List<Task> getAllTasks();

    @Query("SELECT * FROM Task ORDER BY dueDate ASC")
    List<Task> getAllTasksSortedByDueDate();

    @Query("SELECT * FROM Task WHERE id = :id")
    Task getTaskById(int id);

}
