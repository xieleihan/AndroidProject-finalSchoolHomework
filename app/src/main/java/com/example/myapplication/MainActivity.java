package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText inputName, inputClass, inputCourse, inputPhone;
    private Button addBtn, selectBtn, updateBtn, deleteBtn;
    private TextView studentInfoTextView;
    private StudentDataSource dataSource;
    private long studentId = -1;

    private void initializeViews() {
        inputName = findViewById(R.id.inputName);
        inputClass = findViewById(R.id.inputClass);
        inputCourse = findViewById(R.id.inputCom);
        inputPhone = findViewById(R.id.inputTel);
        addBtn = findViewById(R.id.addBtn);
        selectBtn = findViewById(R.id.selectBtn);
        updateBtn = findViewById(R.id.insterBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        studentInfoTextView = findViewById(R.id.studentInfoTextView);

        dataSource = new StudentDataSource(this);
        dataSource.open();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchStudent();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent();
            }
        });
    }

    private boolean validateInputs() {
        if (inputName.getText().toString().trim().isEmpty() ||
                inputClass.getText().toString().trim().isEmpty() ||
                inputCourse.getText().toString().trim().isEmpty() ||
                inputPhone.getText().toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "请填写所有信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addStudent() {
        if (!validateInputs()) return;
        String name = inputName.getText().toString();
        String className = inputClass.getText().toString();
        String course = inputCourse.getText().toString();
        String phone = inputPhone.getText().toString();
        dataSource.addStudent(name, className, course, phone);
        Toast.makeText(MainActivity.this, "信息已添加", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("Range")
    private void searchStudent() {
        String name = inputName.getText().toString();
        Cursor cursor = dataSource.getStudentByName(name);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String info = "姓名: " + cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)) + "\n" +
                    "班级: " + cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CLASS)) + "\n" +
                    "课程: " + cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_COURSE)) + "\n" +
                    "电话: " + cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PHONE));
            studentInfoTextView.setText(info);
            studentId = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID)); // Store studentId for update/delete operations
        } else {
            Toast.makeText(MainActivity.this, "未找到学生信息", Toast.LENGTH_SHORT).show();
            studentInfoTextView.setText("");
            studentId = -1; // Reset studentId
        }
        cursor.close(); // Close the cursor
    }

    private void updateStudent() {
        if (!validateInputs()) return;
        String name = inputName.getText().toString();
        String className = inputClass.getText().toString();
        String course = inputCourse.getText().toString();
        String phone = inputPhone.getText().toString();
        if (studentId != -1) {
            dataSource.updateStudent(studentId, name, className, course, phone);
            Toast.makeText(MainActivity.this, "信息已修改", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "请先查询学生信息", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteStudent() {
        if (studentId != -1) {
            dataSource.deleteStudent(studentId);
            Toast.makeText(MainActivity.this, "信息已删除", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "请先查询学生信息", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
