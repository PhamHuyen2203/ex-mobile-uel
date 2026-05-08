package com.example.k23411tapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorActivity extends AppCompatActivity {

    EditText edtFormular;
    Button btnDel, btnCalculate, btnC, btnCE, btnpercent, btn1_x, btnx2, btnSqrt, btnDiv, btnPlusMinus, btnDecimal;
    TextView txtMC, txtMR, txtMPlus, txtMMinus, txtMS, txtM;
    View.OnClickListener m_click_listener;
    double memory = 0;
    String name_share_ref = "CalculatorInfor";

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences(name_share_ref, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Formular", edtFormular.getText().toString());
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(name_share_ref, MODE_PRIVATE);
        String last_formular = preferences.getString("Formular", "0");
        edtFormular.setText(last_formular);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String formular = edtFormular.getText().toString();
                if (formular.length() > 0 && !formular.equals("0")) {
                    String new_formular = formular.substring(0, formular.length() - 1);
                    if (new_formular.isEmpty()) new_formular = "0";
                    edtFormular.setText(new_formular);
                }
            }
        });

        btnC.setOnClickListener(v -> edtFormular.setText("0"));
        btnCE.setOnClickListener(v -> edtFormular.setText("0"));

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //step 1:
                String formular = edtFormular.getText().toString();
                //step 2:
                String result = "";
                result = call_lib(formular);
                //step 3:
                edtFormular.setText(result);
            }
        });

        btn1_x.setOnClickListener(v -> {
            double val = Double.parseDouble(call_lib(edtFormular.getText().toString()));
            edtFormular.setText(formatResult(1 / val));
        });

        btnx2.setOnClickListener(v -> {
            double val = Double.parseDouble(call_lib(edtFormular.getText().toString()));
            edtFormular.setText(formatResult(val * val));
        });

        btnSqrt.setOnClickListener(v -> {
            double val = Double.parseDouble(call_lib(edtFormular.getText().toString()));
            edtFormular.setText(formatResult(Math.sqrt(val)));
        });

        btnpercent.setOnClickListener(v -> {
             double val = Double.parseDouble(call_lib(edtFormular.getText().toString()));
             edtFormular.setText(formatResult(val / 100));
        });

        btnPlusMinus.setOnClickListener(v -> {
            String f = edtFormular.getText().toString();
            if (f.equals("0")) return;
            if (f.startsWith("-")) edtFormular.setText(f.substring(1));
            else edtFormular.setText("-" + f);
        });

        btnDecimal.setOnClickListener(this::processInputData);
        btnDiv.setOnClickListener(this::processInputData);

        m_click_listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double current = 0;
                try {
                    current = Double.parseDouble(call_lib(edtFormular.getText().toString()));
                } catch (Exception ignored) {}

                if (view.equals(txtMC)) {
                    memory = 0;
                } else if (view.equals(txtMR)) {
                    edtFormular.setText(formatResult(memory));
                } else if (view.equals(txtMPlus)) {
                    memory += current;
                } else if (view.equals(txtMMinus)) {
                    memory -= current;
                } else if (view.equals(txtMS)) {
                    memory = current;
                } else if (view.equals(txtM)) {
                    Toast.makeText(CalculatorActivity.this, "Memory: " + memory, Toast.LENGTH_SHORT).show();
                }
            }
        };
        txtMC.setOnClickListener(m_click_listener);
        txtMR.setOnClickListener(m_click_listener);
        txtMPlus.setOnClickListener(m_click_listener);
        txtMMinus.setOnClickListener(m_click_listener);
        txtMS.setOnClickListener(m_click_listener);
        txtM.setOnClickListener(m_click_listener);
    }

    private String call_lib(String formular) {
        try {
            // Xử lý các ký tự hiển thị sang ký tự toán học
            String temp = formular.replace("×", "*")
                                .replace("÷", "/")
                                .replace("x", "*")
                                .replace(":", "/");
            Expression e = new ExpressionBuilder(temp).build();
            return formatResult(e.evaluate());
        } catch (Exception e) {
            return "0";
        }
    }

    private String formatResult(double res) {
        if (res == (long) res)
            return String.valueOf((long) res);
        return String.valueOf(res);
    }

    private void addViews() {
        edtFormular = findViewById(R.id.edtFormular);
        btnDel = findViewById(R.id.btnDel);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnC = findViewById(R.id.btnC);
        btnCE = findViewById(R.id.btnCE);
        btnpercent = findViewById(R.id.btnpercent);
        btn1_x = findViewById(R.id.btn1_x);
        btnx2 = findViewById(R.id.btnx2);
        btnSqrt = findViewById(R.id.btnSqrt);
        btnDiv = findViewById(R.id.btnDiv);
        btnPlusMinus = findViewById(R.id.btnPlusMinus);
        btnDecimal = findViewById(R.id.btnDecimal);

        txtMC = findViewById(R.id.txtMC);
        txtMR = findViewById(R.id.txtMR);
        txtMPlus = findViewById(R.id.txtMPlus);
        txtMMinus = findViewById(R.id.txtMMinus);
        txtMS = findViewById(R.id.txtMS);
        txtM = findViewById(R.id.txtM);
    }

    public void processInputData(View view) {
        Button btn = (Button) view;
        String new_value = btn.getText().toString();
        String current_value = edtFormular.getText().toString();
        
        if (current_value.equals("0") && !new_value.equals(".")) {
            edtFormular.setText(new_value);
        } else {
            edtFormular.setText(current_value + new_value);
        }
    }
}
