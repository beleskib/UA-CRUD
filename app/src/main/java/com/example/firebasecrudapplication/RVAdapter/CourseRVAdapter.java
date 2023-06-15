package com.example.firebasecrudapplication.RVAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasecrudapplication.Model.CourseModel;
import com.example.firebasecrudapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CourseRVAdapter extends RecyclerView.Adapter<CourseRVAdapter.ViewHolder> {
    int lastPosition = -1;
    private ArrayList<CourseModel> courseModelArrayList;
    private Context context;
    private CourseClickInterface courseClickInterface;


    public CourseRVAdapter(ArrayList<CourseModel> courseModelArrayList, Context context, CourseClickInterface courseClickInterface) {
        this.courseModelArrayList = courseModelArrayList;
        this.context = context;
        this.courseClickInterface = courseClickInterface;
    }

    @NonNull
    @Override
    public CourseRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseRVAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CourseModel courseModel = courseModelArrayList.get(position);
        holder.courseNameTV.setText(courseModel.getCourseName());
        holder.coursePriceTV.setText(courseModel.getCoursePrice() + "MKD");
        Picasso.get().load(courseModel.getCourseImg()).into(holder.courseIV);
        setAnimation(holder.itemView, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseClickInterface.onCourseClick(position);

            }
        });
    }
    private void setAnimation(View itemView, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView courseNameTV, coursePriceTV;
        private ImageView courseIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            coursePriceTV = itemView.findViewById(R.id.idTVPrice);
            courseIV = itemView.findViewById(R.id.idIVCourse);
        }
    }
    public interface CourseClickInterface{
        void onCourseClick(int position);
    }
}
