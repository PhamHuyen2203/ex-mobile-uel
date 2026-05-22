package com.example.k23411tapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.Employee;

public class AddEmployeeActivity extends AppCompatActivity {

    EditText edtID, edtName, edtPhone;
    AutoCompleteTextView actBirthPlace;
    String[] listOfBirthPlace;
    ArrayAdapter<String> adapterBirthPlace;
    Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_employee);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void processSaveEmployee() {
        Employee emp=new Employee();
        emp.setId(edtID.getText().toString());
        emp.setName(edtName.getText().toString());
        emp.setPhone(edtPhone.getText().toString());
        emp.setBirthPlace(actBirthPlace.getText().toString());

        Intent intent = getIntent();
        intent.putExtra("K23411T_EMPLOYEE", emp);
        setResult(888, intent);
        //call finish --> advanced employee --> foreground lifetime
        finish();
    }

    private void addViews() {
        edtID = findViewById(R.id.edtID);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);

        actBirthPlace = findViewById(R.id.actBirthPlace);

        listOfBirthPlace = getResources().getStringArray(R.array.arr_province);

        adapterBirthPlace = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listOfBirthPlace);

        actBirthPlace.setAdapter(adapterBirthPlace);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void addEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSaveEmployee();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}