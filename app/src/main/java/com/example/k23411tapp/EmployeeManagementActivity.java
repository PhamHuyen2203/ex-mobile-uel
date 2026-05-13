package com.example.k23411tapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Random;

public class EmployeeManagementActivity extends AppCompatActivity {

    EditText edtID,edtName,edtPhone;
    Button btnSave,btnClear,btnExit;
    ListView lvEmployee;
    ArrayList<String>ListOfEmployee;
    ArrayAdapter<String>adapterEmployee;
    int selectedIndex = -1;
    String preferenceName = "EmployeePrefs";

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(preferenceName, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("SelectedIndex", selectedIndex);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(preferenceName, MODE_PRIVATE);
        selectedIndex = prefs.getInt("SelectedIndex", -1);
        if (selectedIndex != -1 && selectedIndex < ListOfEmployee.size()) {
            displaySelectedEmployee(selectedIndex);
        }
        adapterEmployee.notifyDataSetChanged();
    }

    private void displaySelectedEmployee(int i) {
        String emp = ListOfEmployee.get(i);
        String[] arrInfor = emp.split(" - ");
        if (arrInfor.length == 3) {
            edtID.setText(arrInfor[0]);
            edtName.setText(arrInfor[1]);
            edtPhone.setText(arrInfor[2]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_management);
        addView();
        sampleData();
        addEvent();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvent() {
        lvEmployee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                selectedIndex = i;
                displaySelectedEmployee(i);
                adapterEmployee.notifyDataSetChanged();
            }
        });
    }

    private void sampleData() {
        ListOfEmployee.add("EMP001 - Nguyen Van A - 0901234567");
        ListOfEmployee.add("EMP002 - Tran Thi B - 0912345678");
        ListOfEmployee.add("EMP003 - Le Van C - 0987654321");
        adapterEmployee.notifyDataSetChanged();
        //hoặc cách thứ 2: Tạo vòng lặp thêm khoảng 1000 nhân viên
        Random random=new Random(); //random phonenumber
        for(int i=0;i<1000;i++) {
            @SuppressLint("DefaultLocale") String id = "EMP" + String.format("%03d", i + 4);
            String name = "Employee " + (i + 4);
            String phone = "090";
            int provider= random.nextInt(3);
            if(provider==1)
                phone = "098";
            else if(provider==2)
                phone = "094";
            for(int p=1;p<=7;p++)
                phone += random.nextInt(10);
            ListOfEmployee.add(id + " - " + name + " - " + phone);
        }
        adapterEmployee.notifyDataSetChanged();
    }

    public void addView() {
        edtID = findViewById(R.id.edtID);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);
        btnExit = findViewById(R.id.btnExit);
        lvEmployee = findViewById(R.id.lvEmployee);
        ListOfEmployee = new ArrayList<>();
        adapterEmployee = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ListOfEmployee) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == selectedIndex) {
                    view.setBackgroundColor(Color.YELLOW);
                } else {
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
                return view;
            }
        };
        lvEmployee.setAdapter(adapterEmployee);
    }

    public void closeActivity(View view) {
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCanceledOnTouchOutside(false);
        ImageView imgYes=dialog.findViewById(R.id.imgYes);
        ImageView imgCancel=dialog.findViewById(R.id.imgCancel);
        imgYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}