package com.example.k23411tapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adapters.ProductAdapter;
import com.example.dals.ProductDAO;
import com.example.models.Product;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    ListView lvProduct;
    ArrayList<Product> products;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        lvProduct = findViewById(R.id.lvProduct);
        TextView txtProductTitle = findViewById(R.id.txtProductTitle);

        String categoryId = getIntent().getStringExtra("CATEGORY_ID");
        if (categoryId != null) {
            products = ProductDAO.getProductsByCategory(ProductActivity.this, categoryId);
            txtProductTitle.setText("Products - " + categoryId);
        } else {
            products = ProductDAO.getProducts(ProductActivity.this);
            txtProductTitle.setText("All Products");
        }

        productAdapter = new ProductAdapter(ProductActivity.this, R.layout.product_custom_item, products);
        lvProduct.setAdapter(productAdapter);
    }
}
