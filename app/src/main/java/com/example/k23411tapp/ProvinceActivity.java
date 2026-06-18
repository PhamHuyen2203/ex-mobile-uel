package com.example.k23411tapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.Province;

import java.util.ArrayList;
import java.util.List;

public class ProvinceActivity extends AppCompatActivity {

    ListView lvProvince;
    ArrayList<Province> provinces;
    ArrayAdapter<Province> provinceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_province);
        addViews();
        addEvents();
        
        loadProvinces();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        lvProvince = findViewById(R.id.lvProvince);
        provinces = new ArrayList<>();
        provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, provinces);
        lvProvince.setAdapter(provinceAdapter);
    }

    private void addEvents() {
        ImageView imgBackProvince = findViewById(R.id.imgBackProvince);
        imgBackProvince.setOnClickListener(v -> finish());

        lvProvince.setOnItemClickListener((parent, view, position, id) -> {
            Province selected = provinceAdapter.getItem(position);
            if (selected != null) {
                Intent intent = new Intent(ProvinceActivity.this, WeatherDetailActivity.class);
                intent.putExtra("PROVINCE_ID", selected.getId());
                intent.putExtra("PROVINCE_NAME", selected.getName());
                startActivity(intent);
            }
        });
    }

    private void loadProvinces() {
        WeatherService.getProvinces(new WeatherService.Callback<List<Province>>() {
            @Override
            public void onSuccess(List<Province> result) {
                runOnUiThread(() -> {
                    provinceAdapter.clear();
                    provinceAdapter.addAll(result);
                });
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> Toast.makeText(ProvinceActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}
