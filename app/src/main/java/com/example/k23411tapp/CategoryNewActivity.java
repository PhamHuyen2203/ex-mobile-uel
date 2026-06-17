package com.example.k23411tapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dals.CategoryDAO;
import com.example.models.Category;

public class CategoryNewActivity extends AppCompatActivity {

    EditText edtCategoryID;
    EditText edtCategoryName;
    EditText edtCategoryDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_new);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        edtCategoryID=findViewById(R.id.edtCategoryID);
        edtCategoryName=findViewById(R.id.edtCategoryName);
        edtCategoryDescription=findViewById(R.id.edtCategoryDescription);
    }

    public void processSaveCategory(View view) {
        String cateID = edtCategoryID.getText().toString();
        String cateName = edtCategoryName.getText().toString();
        String description = edtCategoryDescription.getText().toString();

        if (cateID.isEmpty() || cateName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Category category = new Category(cateID, cateName, description);

        long result = CategoryDAO.saveNewCategory(this, category);
        if (result > 0) {
            Intent intent = getIntent();
            setResult(2, intent);
            Toast.makeText(this, "Success to add category", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Fail to add category", Toast.LENGTH_SHORT).show();
        }
    }

    public void processCancelCategory(View view) {
        finish();
    }
}