package com.example.firebasecrudapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import java.util.HashMap;
import java.util.Map;

public class EditCourseActivity extends AppCompatActivity {

    private Button updateCourseBtn, deleteCourseBtn;
    private TextInputEditText courseNameEdt, courseDescEdt, coursePriceEdt, bestSuitedEdt, courseImgEdt, courseLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar loadingPB;
    private String courseID;
    private CourseModel courseModel;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        firebaseDatabase = firebaseDatabase.getInstance();
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        courseDescEdt = findViewById(R.id.idEdtCourseDescription);
        coursePriceEdt = findViewById(R.id.idEdtCoursePrice);
        bestSuitedEdt = findViewById(R.id.idEdtSuitedFor);
        courseImgEdt = findViewById(R.id.idEdtCourseImageLink);
        courseLinkEdt = findViewById(R.id.idEdtCourseLink);
        loadingPB = findViewById(R.id.idPBLoading);
        updateCourseBtn = (Button) findViewById(R.id.idBtnUpdateCourse);
        deleteCourseBtn = (Button) findViewById(R.id.idBtnDeleteCourse);

        courseModel = getIntent().getParcelableExtra("course");
        if(courseModel != null) {
            courseNameEdt.setText(courseModel.getCourseName());
            coursePriceEdt.setText(courseModel.getCoursePrice());
            bestSuitedEdt.setText(courseModel.getBestSuitedFor());
            courseImgEdt.setText(courseModel.getCourseImg());
            courseLinkEdt.setText(courseModel.getCourseLink());
            courseDescEdt.setText(courseModel.getCourseDescription());

            courseID = courseModel.getCourseID();
        }

        databaseReference = firebaseDatabase.getReference("Courses").child(courseID);

        updateCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                String courseName = courseNameEdt.getText().toString();
                String coursePrice = coursePriceEdt.getText().toString();
                String coursesuitedFor = bestSuitedEdt.getText().toString();
                String courseLink = courseLinkEdt.getText().toString();
                String courseDesc = courseDescEdt.getText().toString();
                String courseImg = courseImgEdt.getText().toString();

                Map<String, Object> map = new HashMap<>();
                map.put("courseName", courseName);
                map.put("courseDescription", courseDesc);
                map.put("coursePrice", coursePrice);
                map.put("bestSuitedFor", coursesuitedFor);
                map.put("courseImg", courseImg);
                map.put("courseLink", courseLink);
                map.put("courseID", courseID);
                
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        databaseReference.updateChildren(map);
                        Toast.makeText(EditCourseActivity.this, "Course updated...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditCourseActivity.this, MainActivity.class));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditCourseActivity.this, "Failed..", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });

        deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCourse();
            }
        });
    }
    private void deleteCourse() {
        databaseReference.removeValue();
        Toast.makeText(this, "Course deleted..", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditCourseActivity.this, MainActivity.class));
    }
}