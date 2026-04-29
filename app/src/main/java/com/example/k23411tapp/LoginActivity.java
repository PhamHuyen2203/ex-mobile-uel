package com.example.k23411tapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    
    EditText edtUsername;
    EditText edtPassword;
    TextView txtMessage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        addViews ();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        edtUsername=findViewById(R.id.edtUsername);
        edtPassword=findViewById(R.id.edtPassword);
        txtMessage=findViewById(R.id.txtMessage);
    }

    public void loginSystem(View view) {
        String username=edtUsername.getText().toString();
        String pw=edtPassword.getText().toString();
        if(username.equalsIgnoreCase("admin") && pw.equals("123"))
        {
            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            txtMessage.setText(R.string.str_login_success);
        }
        else
        {
            txtMessage.setText(R.string.str_login_failed);
        }
    }

    public void exitSystem(View view) {
        finish();
    }
}