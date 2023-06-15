package com.example.firebasecrudapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.firebasecrudapplication.Model.CourseModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCourseActivity extends AppCompatActivity {

    private Button addCourseBtn;
    private TextInputEditText courseNameEdt, courseDescEdt, coursePriceEdt, bestSuitedEdt, courseImgEdt, courseLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar loadingPB;
    private String courseID;
    private CourseModel courseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        addCourseBtn = findViewById(R.id.idBtnAddCourse);
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        courseDescEdt = findViewById(R.id.idEdtCourseDescription);
        coursePriceEdt = findViewById(R.id.idEdtCoursePrice);
        bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
        courseImgEdt = findViewById(R.id.idEdtCourseImageLink);
        courseLinkEdt = findViewById(R.id.idEdtCourseLink);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");

        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                String courseName = courseNameEdt.getText().toString();
                String coursePrice = coursePriceEdt.getText().toString();
                String coursesuitedFor = bestSuitedEdt.getText().toString();
                String courseLink = courseLinkEdt.getText().toString();
                String courseDesc = courseDescEdt.getText().toString();
                String courseImg = courseImgEdt.getText().toString();
                courseID = courseName;
/*
                courseID = sanitizeCourseName(courseName);
*/
                courseModel = new CourseModel(courseName, courseDesc, coursePrice, coursesuitedFor, courseImg, courseLink, courseID);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        databaseReference.child(courseID).setValue(courseModel);
                        Toast.makeText(AddCourseActivity.this, "Course Added ...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddCourseActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddCourseActivity.this, "Error is "+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
   /* private String sanitizeCourseName(String courseName) {
        return courseName.replaceAll("[.#$\\[\\]]", "_");
    }*/
}