package com.example.k23411tapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.UserAccount;

public class MainActivity extends AppCompatActivity {

    TextView txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        txtWelcome = findViewById(R.id.txtWelcome);
        //step 1: get intent from Login
        Intent intent = getIntent();
        //step 2: extract data and update UI
        UserAccount account=(UserAccount) intent.getSerializableExtra("USER_ACCOUNT");
        if(account!=null) {
            txtWelcome.setText(txtWelcome.toString());
        }
    }

    public void say_hello(View view) {
        Toast.makeText( this,"Hello K23411T",
                Toast.LENGTH_LONG).show();
    }

    public void exit_app(View view) {
        finish();
    }

    public void show_my_major(View view) {
        //String my_major="Data Science!!!";
        String my_major=getString(R.string.str_my_major);
        Toast.makeText(this,my_major, Toast.LENGTH_LONG).show();
    }

    public void openCalculatorApp (View view) {
        Intent intent=new Intent(MainActivity.this, CalculatorActivity.class);
        startActivity(intent);
    }
}