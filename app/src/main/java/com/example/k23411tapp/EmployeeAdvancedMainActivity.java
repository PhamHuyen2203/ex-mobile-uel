package com.example.k23411tapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adapters.EmployeeAdapter;
import com.example.models.Department;
import com.example.models.Employee;

import java.util.ArrayList;

public class EmployeeAdvancedMainActivity extends AppCompatActivity {

    ListView lvEmployee;
    EmployeeAdapter adapterEmployee;

    Spinner spDepartment;
    ArrayList<Department> listOfDepartment;
    ArrayAdapter<Department> adapterDepartment;
    ImageView imgAddEmployee, imgEditEmployee, imgDeleteEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_advanced_main);
        addViews();
        sampleData();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Department selectedDepartment = listOfDepartment.get(i);
                adapterEmployee.clear();
                adapterEmployee.addAll(selectedDepartment.getListOfEmployee());
                adapterEmployee.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        imgAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeAdvancedMainActivity.this, AddEmployeeActivity.class);
                startActivityForResult(intent,999);
            }
        });
    }

    private void sampleData() {
        listOfDepartment.clear();

        // 1. Tạo phòng ban "All" đầu tiên
        Department allDept = new Department("ALL", "All");
        listOfDepartment.add(allDept);

        // 2. Thêm các phòng ban khác
        Department D01 = new Department("D01", "Human Resources");
        D01.addEmployee(new Employee("EMP001", "Nguyen Van A", "0901234567"));
        D01.addEmployee(new Employee("EMP002", "Tran Thi B", "0912345678"));
        listOfDepartment.add(D01);

        Department D02 = new Department("D02", "Technical");
        D02.addEmployee(new Employee("EMP003", "Le Van C", "0987654321"));
        listOfDepartment.add(D02);

        Department D03 = new Department("D03", "Sales");
        D03.addEmployee(new Employee("EMP004", "Pham Van D", "0944123456"));
        D03.addEmployee(new Employee("EMP005", "Hoang Thi E", "0966987654"));
        listOfDepartment.add(D03);

        Department D04 = new Department("D04", "Marketing");
        D04.addEmployee(new Employee("EMP006", "Ngo Van F", "0933111222"));
        listOfDepartment.add(D04);

        Department D05 = new Department("D05", "Accounting");
        D05.addEmployee(new Employee("EMP007", "Dang Thi G", "0977333444"));
        listOfDepartment.add(D05);

        Department D06 = new Department("D06", "IT Support");
        D06.addEmployee(new Employee("EMP008", "Vu Van H", "0988555666"));
        listOfDepartment.add(D06);

        // 3. Gom tất cả nhân viên vào phòng ban "All"
        for (int i = 1; i < listOfDepartment.size(); i++) {
            allDept.getListOfEmployee().addAll(listOfDepartment.get(i).getListOfEmployee());
        }

        adapterDepartment.notifyDataSetChanged();
    }

    private void addViews() {
        lvEmployee = findViewById(R.id.lvEmployee);
        adapterEmployee = new EmployeeAdapter(this, R.layout.item_custom_employee);
        lvEmployee.setAdapter(adapterEmployee);

        spDepartment = findViewById(R.id.spDepartment);
        listOfDepartment = new ArrayList<>();
        adapterDepartment = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listOfDepartment);
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartment.setAdapter(adapterDepartment);

        imgAddEmployee = findViewById(R.id.imgAddEmployee);
        imgEditEmployee = findViewById(R.id.imgEditEmployee);
        imgDeleteEmployee = findViewById(R.id.imgDeleteEmployee);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == 888 && data != null) {
            Employee emp = (Employee) data.getSerializableExtra("K23411T_EMPLOYEE");
            if (emp == null) return;

            int selectedPos = spDepartment.getSelectedItemPosition();
            Department targetDept;

            if (selectedPos == 0) {
                // Nếu chọn "All", đưa vào "Human Resources" (index 1)
                targetDept = listOfDepartment.get(1);
            } else {
                // Nếu chọn phòng ban cụ thể, đưa vào chính nó
                targetDept = listOfDepartment.get(selectedPos);
            }

            // Thêm vào phòng ban đích
            targetDept.addEmployee(emp);

            // Cập nhật luôn vào phòng ban "All" (index 0) để đồng bộ
            listOfDepartment.get(0).addEmployee(emp);

            // Cập nhật lại ListView hiển thị cho phòng ban đang chọn
            Department currentDisplayDept = listOfDepartment.get(selectedPos);
            adapterEmployee.clear();
            adapterEmployee.addAll(currentDisplayDept.getListOfEmployee());
            adapterEmployee.notifyDataSetChanged();
        }
    }
}