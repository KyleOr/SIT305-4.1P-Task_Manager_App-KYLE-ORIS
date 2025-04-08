package com.example.sit305_41p_taskmanagerapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextDueDate;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_task);

        // Initialize UI components
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDueDate = findViewById(R.id.editTextDueDate);
        buttonSave = findViewById(R.id.buttonSave);

        // Save button click listener
        buttonSave.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
        // Get text from input fields
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String dueDate = editTextDueDate.getText().toString().trim();

        // Basic validation
        if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create and insert the Task
        Task task = new Task();
        task.title = title;
        task.description = description;
        task.dueDate = dueDate;

        // Save to Room database
        TaskDatabase.getInstance(this).taskDao().insert(task);

        Toast.makeText(this, "Task saved!", Toast.LENGTH_SHORT).show();

        // Close activity
        finish();
    }
}
