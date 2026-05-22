package com.example.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.k23411tapp.R;
import com.example.models.Employee;

public class EmployeeAdapter extends ArrayAdapter<Employee> {

    Activity context;
    int resource;
    public EmployeeAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View custom_view = convertView;
        if (custom_view == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            custom_view = inflater.inflate(this.resource, parent, false);
        }

        //lấy dữ liệu đang nhân bản ở dòng thứ position
        Employee emp = getItem(position);

        if (emp != null) {
            //gán dữ liệu vào giao diện
            TextView txtID = custom_view.findViewById(R.id.txtID);
            TextView txtName = custom_view.findViewById(R.id.txtName);
            TextView txtPhone = custom_view.findViewById(R.id.txtPhone);

            txtID.setText(emp.getId());
            txtName.setText(emp.getName());
            txtPhone.setText(emp.getPhone());

            ImageView imgCall=custom_view.findViewById(R.id.imgCall);
            ImageView imgSMS=custom_view.findViewById(R.id.imgSMS);
            imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentCall=new Intent(Intent.ACTION_DIAL);
                    Uri uri=Uri.parse("tel:"+emp.getPhone());
                    intentCall.setData(uri);
                    context.startActivity(intentCall);
                }
            });
        }

        return custom_view;
    }
}
