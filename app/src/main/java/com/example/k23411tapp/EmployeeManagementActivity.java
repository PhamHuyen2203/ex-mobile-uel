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
import android.widget.Toast;

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
        editor.clear(); // Xóa dữ liệu cũ để lưu lại danh sách mới nhất
        
        editor.putInt("SelectedIndex", selectedIndex);
        // Lưu số lượng nhân viên
        editor.putInt("EmployeeCount", ListOfEmployee.size());
        // Lưu từng nhân viên theo index
        for (int i = 0; i < ListOfEmployee.size(); i++) {
            editor.putString("Employee_" + i, ListOfEmployee.get(i));
        }
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(preferenceName, MODE_PRIVATE);
        
        // Đọc số lượng nhân viên đã lưu (mặc định là -1 nếu chưa từng lưu)
        int count = prefs.getInt("EmployeeCount", -1);
        
        if (count != -1) {
            ListOfEmployee.clear(); // Xóa dữ liệu tạm thời
            for (int i = 0; i < count; i++) {
                String emp = prefs.getString("Employee_" + i, "");
                if (!emp.isEmpty()) {
                    ListOfEmployee.add(emp);
                }
            }
        } else {
            // Nếu là lần đầu chạy app (chưa có count), nạp dữ liệu mẫu
            if (ListOfEmployee.isEmpty()) {
                sampleData();
            }
        }

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
        // sampleData(); // Đã chuyển vào onResume để xử lý thông minh hơn
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
        for(int i=0;i<10;i++) {
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
        btnClear = findViewById(R.id.btnDelete);
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

    public void saveEmployee (View view) {
        String id = edtID.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        if (id.isEmpty()) {
            edtID.setError(getString(R.string.str_error_id_empty));
            return;
        }

        String emp = id + " - " + name + " - " + phone;
        int foundIndex = -1;

        // Tìm xem ID đã tồn tại trong danh sách chưa
        for (int i = 0; i < ListOfEmployee.size(); i++) {
            if (ListOfEmployee.get(i).startsWith(id + " - ")) {
                foundIndex = i;
                break;
            }
        }

        if (foundIndex != -1) {
            // Nếu đã tồn tại thì cập nhật
            ListOfEmployee.set(foundIndex, emp);
            selectedIndex = foundIndex;
        } else {
            // Nếu chưa tồn tại thì thêm mới
            ListOfEmployee.add(emp);
            selectedIndex = ListOfEmployee.size() - 1;
        }

        adapterEmployee.notifyDataSetChanged();
        lvEmployee.smoothScrollToPosition(selectedIndex);
    }

    public void deleteEmployee(View view) {
        if (selectedIndex == -1) {
            Toast.makeText(this, R.string.str_toast_select_employee, Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.str_confirm_delete_title);
        builder.setMessage(R.string.str_confirm_delete_message);
        builder.setPositiveButton(R.string.str_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListOfEmployee.remove(selectedIndex);
                selectedIndex = -1;
                adapterEmployee.notifyDataSetChanged();
                // Xóa nội dung trên các EditText sau khi xóa
                edtID.setText("");
                edtName.setText("");
                edtPhone.setText("");
                Toast.makeText(EmployeeManagementActivity.this, R.string.str_toast_delete_success, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.str_no, null);
        builder.show();
    }
}