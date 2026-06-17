package com.example.k23411tapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.DataWarehouse;
import com.example.models.Product;

import java.util.ArrayList;

public class MultiThreadObjectActivity extends AppCompatActivity {
    EditText edtNumberProduct;
    Button btnDownload;
    TextView txtPercent;
    ListView lvProduct;
    ArrayList<Product> products;
    ArrayAdapter<Product> adapterProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_multi_thread_object);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processDownloadProduct();
            }
        });
        lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                if(i>=0 && i<products.size()) {
                    products.remove(i);
                    adapterProduct.notifyDataSetChanged();
                }
                return false;
            }
        });
    }
    //Main Thread
    Handler mainThread = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int percent = msg.arg1;
            txtPercent.setText(percent + " %");
            if (msg.obj != null && msg.obj instanceof Product) {
                products.add((Product) msg.obj);
                adapterProduct.notifyDataSetChanged();
            }
            if (percent==100) {
                Toast.makeText(MultiThreadObjectActivity.this, "Download complete", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    private void processDownloadProduct() {
        if (edtNumberProduct.getText().toString().isEmpty()) {
            edtNumberProduct.setError("Please enter a number");
            return;
        }
        int n = Integer.parseInt(edtNumberProduct.getText().toString());
        //tiểu trình
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= n; i++) {
                    Product product = DataWarehouse.downloadProduct(i);
                    int percent = i * 100 / n;
                    Message message = mainThread.obtainMessage();
                    //gán các giá tr cho message
                    message.arg1=percent;
                    message.obj=product;
                    //gửi lại message cho mainthread
                    mainThread.sendMessage(message);
                    //cần tạm dừng tiểu trình để tiếng trình khác có thể thực hiện
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message finalMessage = mainThread.obtainMessage();
                finalMessage.arg1 = 100;
                mainThread.sendMessage(finalMessage);
            }
        });
        thread.start(); //kích hoạt tiểu trình để chạy longtime task
    }

    private void addViews() {
        edtNumberProduct = findViewById(R.id.edtNumberProduct);
        btnDownload = findViewById(R.id.btnDownload);
        txtPercent = findViewById(R.id.txtPercent);
        lvProduct = findViewById(R.id.lvProduct);
        products = new ArrayList<>();
        adapterProduct = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        lvProduct.setAdapter(adapterProduct);
    }
}