package com.example.k23411tapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adapters.CategoryAdapter;
import com.example.dals.CategoryDAO;
import com.example.models.Category;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    ListView lvCategory;
    ArrayList<Category> categories;
    CategoryAdapter categoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCate = categoryAdapter.getItem(position);
                Intent intent = new Intent(CategoryActivity.this, ProductActivity.class);
                intent.putExtra("CATEGORY_ID", selectedCate.getCategoryID());
                startActivity(intent);
            }
        });
    }

    private void addViews() {
        lvCategory = findViewById(R.id.lvCategory);
        categories = CategoryDAO.getCategories(CategoryActivity.this);
        categoryAdapter = new CategoryAdapter(CategoryActivity.this, R.layout.category_custom_item);
        categoryAdapter.addAll(categories);
        lvCategory.setAdapter(categoryAdapter);
    }
}
