package com.example.sit305_41p_taskmanagerapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);

        // Initialize UI components as local variables
        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        EditText editTextDueDate = findViewById(R.id.editTextDueDate);
        Button buttonSave = findViewById(R.id.buttonSave);

        // Set onClickListener for the Due Date EditText
        editTextDueDate.setOnClickListener(v -> showDatePickerDialog(editTextDueDate));

        // Save button click listener
        buttonSave.setOnClickListener(v -> addTask(editTextTitle, editTextDescription, editTextDueDate));
    }

    private void addTask(EditText editTextTitle, EditText editTextDescription, EditText editTextDueDate) {
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

    private void showDatePickerDialog(EditText editTextDueDate) {
        // Get current date to pre-fill the DatePicker
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddTaskActivity.this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    // Format selected date to yyyy-MM-dd
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = sdf.format(selectedDate.getTime());

                    // Set formatted date to EditText
                    editTextDueDate.setText(formattedDate);
                },
                year, month, dayOfMonth
        );

        datePickerDialog.show();
    }
}
