package com.example.sit305_41p_taskmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set up the view insets to avoid overlap with system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        // Floating Action Button to add new task
        FloatingActionButton fab = findViewById(R.id.fabAddTask);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        // Sort Button click logic
        Button buttonSortByDate = findViewById(R.id.buttonSortByDate);
        buttonSortByDate.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Sorting by date...", Toast.LENGTH_SHORT).show();
            sortTasksByDate(); // Call the sort method
        });

        loadTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks(); // Reload tasks when returning to this screen
    }

    private void loadTasks() {
        // Fetch all tasks from the database
        taskList = TaskDatabase.getInstance(this)
                .taskDao()
                .getAllTasks();

        // Initialize adapter if not already done
        if (taskAdapter == null) {
            taskAdapter = new TaskAdapter(taskList);
            recyclerViewTasks.setAdapter(taskAdapter);
        } else {
            taskAdapter.setTasks(taskList);
        }
    }

    private void sortTasksByDate() {
        if (taskList == null || taskList.isEmpty()) {
            Toast.makeText(this, "No tasks available to sort.", Toast.LENGTH_SHORT).show();
            return;
        }

        taskList.sort(Comparator.comparing(task -> task.dueDate));
        taskAdapter.setTasks(taskList);

        // Debugging
        Toast.makeText(this, "Sorted first date: " + taskList.get(0).dueDate, Toast.LENGTH_LONG).show();
    }

}
