package com.example.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.models.Course;
import com.example.k23411tapp.R;

import java.util.List;

public class CourseAdapter extends ArrayAdapter<Course> {
    public CourseAdapter(@NonNull Context context, int resource, @NonNull List<Course> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Course course = getItem(position);
        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        if (course != null) {
            text1.setText(course.getName());
            text2.setText(String.format("Mã: %s | Tín chỉ: %s | Học kỳ: %s", 
                course.getCode(), course.getCredits(), course.getSemester()));
        }

        return convertView;
    }
}
