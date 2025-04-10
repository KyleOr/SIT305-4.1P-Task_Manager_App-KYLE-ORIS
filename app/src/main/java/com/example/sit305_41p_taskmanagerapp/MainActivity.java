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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    private enum SortMode {
        DATE, TITLE_ASC, TITLE_DESC
    }

    private SortMode currentSortMode = SortMode.DATE;
    private Button buttonSortByMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fabAddTask);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        // Sort button setup
        buttonSortByMode = findViewById(R.id.buttonSortByDate);
        buttonSortByMode.setOnClickListener(v -> {
            cycleSortMode(); // Cycle sort mode
            sortTasks();     // Apply sort
        });

        loadTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks(); // Refresh list
    }

    private void loadTasks() {
        taskList = TaskDatabase.getInstance(this)
                .taskDao()
                .getAllTasks();

        if (taskAdapter == null) {
            taskAdapter = new TaskAdapter(taskList);
            recyclerViewTasks.setAdapter(taskAdapter);
        } else {
            taskAdapter.setTasks(taskList);
        }

        sortTasks(); // Apply current sort mode
    }

    private void cycleSortMode() {
        switch (currentSortMode) {
            case DATE:
                currentSortMode = SortMode.TITLE_ASC;
                break;
            case TITLE_ASC:
                currentSortMode = SortMode.TITLE_DESC;
                break;
            case TITLE_DESC:
                currentSortMode = SortMode.DATE;
                break;
        }

        updateSortButtonText(); // Update button text when sort mode changes
    }

    private void updateSortButtonText() {
        String nextModeText;
        switch (currentSortMode) {
            case DATE:
                nextModeText = "Sort: Due Date";
                break;
            case TITLE_ASC:
                nextModeText = "Sort: Title A–Z";
                break;
            case TITLE_DESC:
                nextModeText = "Sort: Title Z–A";
                break;
            default:
                nextModeText = "Sort";
        }
        buttonSortByMode.setText(nextModeText);
    }

    private void sortTasks() {
        if (taskList == null || taskList.isEmpty()) {
            Toast.makeText(this, "No tasks to sort.", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (currentSortMode) {
            case DATE:
                taskList.sort(Comparator.comparing(task -> task.dueDate));
                break;
            case TITLE_ASC:
                taskList.sort(Comparator.comparing(task -> task.title.toLowerCase(Locale.getDefault())));
                break;
            case TITLE_DESC:
                taskList.sort((t1, t2) -> t2.title.toLowerCase(Locale.getDefault())
                        .compareTo(t1.title.toLowerCase(Locale.getDefault())));
                break;
        }

        taskAdapter.setTasks(taskList);
    }
}
