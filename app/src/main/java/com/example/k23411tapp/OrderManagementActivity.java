package com.example.k23411tapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.adapters.OrderAdapter;
import com.example.models.DataWarehouse;
import com.example.models.Order;
import com.example.models.OrderStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderManagementActivity extends AppCompatActivity {

    TextView txtFromDate, txtToDate;
    ImageView imgFromDate, imgToDate, imgFilter, imgClearFilter;
    ListView lvOrder;
    ArrayList<Order> orders;
    OrderAdapter orderAdapter;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    Calendar calFromDate = Calendar.getInstance();
    Calendar calToDate = Calendar.getInstance();
    OrderStatus currentStatus = OrderStatus.ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_management);
        
        // Mặc định lọc từ đầu năm 2024 đến nay
        calFromDate.set(2024, Calendar.JANUARY, 1);
        
        addViews();
        addEvents();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        DatePickerDialog.OnDateSetListener fromDateListener = (view, year, month, dayOfMonth) -> {
            calFromDate.set(year, month, dayOfMonth);
            txtFromDate.setText(sdf.format(calFromDate.getTime()));
        };
        DatePickerDialog.OnDateSetListener toDateListener = (view, year, month, dayOfMonth) -> {
            calToDate.set(year, month, dayOfMonth);
            txtToDate.setText(sdf.format(calToDate.getTime()));
        };

        imgFromDate.setOnClickListener(v -> new DatePickerDialog(this, fromDateListener, 
            calFromDate.get(Calendar.YEAR), calFromDate.get(Calendar.MONTH), calFromDate.get(Calendar.DAY_OF_MONTH)).show());

        imgToDate.setOnClickListener(v -> new DatePickerDialog(this, toDateListener, 
            calToDate.get(Calendar.YEAR), calToDate.get(Calendar.MONTH), calToDate.get(Calendar.DAY_OF_MONTH)).show());

        imgClearFilter.setOnClickListener(v -> {
            currentStatus = OrderStatus.ALL;
            calFromDate.set(2024, Calendar.JANUARY, 1);
            calToDate = Calendar.getInstance();
            txtFromDate.setText(sdf.format(calFromDate.getTime()));
            txtToDate.setText(sdf.format(calToDate.getTime()));
            refreshData();
        });

        imgFilter.setOnClickListener(v -> refreshData());
    }

    private void refreshData() {
        ArrayList<Order> filtered = DataWarehouse.filterOrders(currentStatus, calFromDate.getTime(), calToDate.getTime());
        orders.clear();
        orders.addAll(filtered);
        orderAdapter.notifyDataSetChanged();
    }

    private void addViews() {
        txtFromDate = findViewById(R.id.txtFromDate);
        txtToDate = findViewById(R.id.txtToDate);
        imgFromDate = findViewById(R.id.imgFromDate);
        imgToDate = findViewById(R.id.imgToDate);
        imgFilter = findViewById(R.id.imgFilter);
        imgClearFilter = findViewById(R.id.imgClearFilter);
        lvOrder = findViewById(R.id.lvOrder);

        txtFromDate.setText(sdf.format(calFromDate.getTime()));
        txtToDate.setText(sdf.format(calToDate.getTime()));

        orders = new ArrayList<>(DataWarehouse.filterOrders(currentStatus, calFromDate.getTime(), calToDate.getTime()));
        orderAdapter = new OrderAdapter(this, R.layout.order_custom_item, orders);
        lvOrder.setAdapter(orderAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnu_order_status_all) currentStatus = OrderStatus.ALL;
        else if (id == R.id.mnu_order_status_completed) currentStatus = OrderStatus.COMPLETED;
        else if (id == R.id.mnu_order_status_not_payment) currentStatus = OrderStatus.NOT_PAYMENT;
        else if (id == R.id.mnu_order_status_on_logistics) currentStatus = OrderStatus.ON_LOGISTICS;
        else if (id == R.id.mnu_order_status_complaint) currentStatus = OrderStatus.COMPLAINT;
        
        refreshData();
        return true;
    }
}
