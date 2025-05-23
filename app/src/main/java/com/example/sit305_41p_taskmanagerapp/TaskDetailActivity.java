package com.example.sit305_41p_taskmanagerapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskDetailActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextDueDate;
    private Button buttonEditTask;

    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextTitle = findViewById(R.id.editTextDetailTitle);
        editTextDescription = findViewById(R.id.editTextDetailDescription);
        editTextDueDate = findViewById(R.id.editTextDetailDueDate);
        buttonEditTask = findViewById(R.id.buttonEditTask);

        // Get task id from intent
        int taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId != -1) {
            Task currentTask = TaskDatabase.getInstance(this).taskDao().getTaskById(taskId);
            if (currentTask != null) {
                editTextTitle.setText(currentTask.title);
                editTextDescription.setText(currentTask.description);
                editTextDueDate.setText(currentTask.dueDate);

                findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
            }
        }

        // Handle DatePickerDialog
        editTextDueDate.setOnClickListener(v -> showDatePickerDialog());

        buttonEditTask.setOnClickListener(v -> {
            if (!isEditing) {
                enableEditing(true);
                buttonEditTask.setText(R.string.save_task);
                isEditing = true;
            } else {
                saveChanges();
                enableEditing(false);
                buttonEditTask.setText(R.string.edit_task);
                isEditing = false;
            }
        });
        Button buttonDeleteTask = findViewById(R.id.buttonDeleteTask);
        buttonDeleteTask.setOnClickListener(v -> deleteTask());
    }

    private void enableEditing(boolean enabled) {
        editTextTitle.setEnabled(enabled);
        editTextDescription.setEnabled(enabled);
        editTextDueDate.setEnabled(enabled);
    }

    private void saveChanges() {
        String updatedTitle = editTextTitle.getText().toString().trim();
        String updatedDescription = editTextDescription.getText().toString().trim();
        String updatedDueDate = editTextDueDate.getText().toString().trim();

        if (updatedTitle.isEmpty() || updatedDescription.isEmpty() || updatedDueDate.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        int taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId != -1) {
            Task currentTask = TaskDatabase.getInstance(this).taskDao().getTaskById(taskId);
            if (currentTask != null) {
                currentTask.title = updatedTitle;
                currentTask.description = updatedDescription;
                currentTask.dueDate = updatedDueDate;

                TaskDatabase.getInstance(this).taskDao().update(currentTask);
                Toast.makeText(this, R.string.task_updated, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteTask() {
        int taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId != -1) {
            Task currentTask = TaskDatabase.getInstance(this).taskDao().getTaskById(taskId);
            if (currentTask != null) {
                TaskDatabase.getInstance(this).taskDao().delete(currentTask);
                Toast.makeText(this, R.string.task_deleted, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                TaskDetailActivity.this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = sdf.format(selectedDate.getTime());

                    editTextDueDate.setText(formattedDate);
                },
                year, month, dayOfMonth
        );

        datePickerDialog.show();
    }
}
