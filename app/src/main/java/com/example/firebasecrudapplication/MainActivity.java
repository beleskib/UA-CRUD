package com.example.firebasecrudapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasecrudapplication.Model.CourseModel;
import com.example.firebasecrudapplication.RVAdapter.CourseRVAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CourseRVAdapter.CourseClickInterface{

    private RecyclerView courseRV;
    private ProgressBar loadingPB;
    private FloatingActionButton addFAB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<CourseModel> courseModelArrayList;
    private RelativeLayout bottomsheetRL;
    private CourseRVAdapter  courseRVAdapter;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        courseRV = (RecyclerView) findViewById(R.id.idRVCourses);
        loadingPB = (ProgressBar) findViewById(R.id.idPBLoading1);
        addFAB = (FloatingActionButton) findViewById(R.id.idAddFAB);
        bottomsheetRL = findViewById(R.id.idRLBSheet);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");
        courseModelArrayList = new ArrayList<>();
        courseRVAdapter = new CourseRVAdapter(courseModelArrayList, (Context) this, this::onCourseClick);
        courseRV.setLayoutManager(new LinearLayoutManager(this));
        courseRV.setAdapter(courseRVAdapter);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, AddCourseActivity.class));
            }
        });
        getAllCourses();
    }

    private void getAllCourses() {
        courseModelArrayList.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                if (snapshot.getValue() instanceof String) {
                    // Handle the case when the value is a String instead of a CourseModel object
                    // You can log an error or handle it according to your requirements
                    return;
                }

                CourseModel courseModel = snapshot.getValue(CourseModel.class);
                courseModelArrayList.add(courseModel);
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                loadingPB.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.idLogOut) {
            Toast.makeText(this, "User logged out...", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            this.finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    public void onCourseClick(int position) {
        displayBottomSheet(courseModelArrayList.get(position));
    }
    private void displayBottomSheet(CourseModel courseModel) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog,bottomsheetRL);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        TextView courseNameTV = layout.findViewById(R.id.idTVCourseName);
        TextView courseDescription = layout.findViewById(R.id.idTVDescription);
        TextView courseSuitedForTV = layout.findViewById(R.id.idTVSuitedforPrice);
        TextView coursePrice = layout.findViewById(R.id.idTVPrice);
        ImageView courseImg = layout.findViewById(R.id.idIVCourse);
        Button editBtn = layout.findViewById(R.id.idbtnEdit);
        Button viewDetailsBtn = layout.findViewById(R.id.idbtnViewDetails);

        courseNameTV.setText(courseModel.getCourseName());
        courseDescription.setText(courseModel.getCourseDescription());
        courseSuitedForTV.setText(courseModel.getBestSuitedFor());
        coursePrice.setText(courseModel.getCoursePrice());
        Picasso.get().load(courseModel.getCourseImg()).into(courseImg);


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, EditCourseActivity.class);
                intent.putExtra("course", courseModel);
                startActivity(intent);
            }
        });
        viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseLink = courseModel.getCourseLink();
                if (!courseLink.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(courseLink));
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid course link", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}