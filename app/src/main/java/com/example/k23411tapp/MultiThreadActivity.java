package com.example.k23411tapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Random;

public class MultiThreadActivity extends AppCompatActivity {

    EditText edtNumberOfButton;
    TextView txtPercent;
    ProgressBar progressBarPercent;
    LinearLayout LinearLayoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_multi_thread);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        edtNumberOfButton = findViewById(R.id.edtNumberOfButton);
        txtPercent = findViewById(R.id.txtPercent);
        progressBarPercent = findViewById(R.id.progressBarPercent);
        LinearLayoutButton = findViewById(R.id.LinearLayoutButton);
    }

    Handler mainThread = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            int value = message.arg1;
            int percent = message.arg2;
            txtPercent.setText(percent + " %");
            progressBarPercent.setProgress(percent);

            Button btn = new Button(MultiThreadActivity.this);
            btn.setWidth(300);
            btn.setHeight(50);
            btn.setText(value+"");
            LinearLayoutButton.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    b.setTextColor(Color.RED);
                }
            });
            return false;
        }
    });

    public void processMultiThreading(View view) {
        int n = Integer.parseInt(edtNumberOfButton.getText().toString());
        //khai báo tiểu trình (đa tiến trình chạy background longtime)
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                //xử lý longtime task ở đây
                //trong đây không được truy suất tới bất kỳ biến Views (GUI) nào
                //nó phải gửi thông điệp về cho MainThread xử lý Visualization
                Random random=new Random();
                for(int i=1;i<=n;i++){
                    int value = random.nextInt(100);
                    int percent = i*100/n;
                    //lấy message từ MainThread
                    Message message = mainThread.obtainMessage();
                    //gán giá trị mới cho message
                    message.arg1=value; //giả sử lưu giá trị vào arg1
                    message.arg2=percent; //giả sử lưu tỉ lệ thực hiện vào arg2
                    //gửi message tới MainThread
                    mainThread.sendMessage(message);
                    //cần tạm thời sleep một thời gian để các tiến trình khác
                    //có thể thực hiện
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //kích hoạt tiểu trình
        th.start();
    }
}