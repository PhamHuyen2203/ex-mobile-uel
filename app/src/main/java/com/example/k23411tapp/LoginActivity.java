package com.example.k23411tapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.ListUserAccount;
import com.example.models.UserAccount;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LoginActivity extends AppCompatActivity {
    
    EditText edtUsername;
    EditText edtPassword;
    TextView txtMessage;
    CheckBox chkSaveInfor;
    String name_share_ref="LoginInfor";

    RadioButton radAdministrator, radEmployee;

    public static final String DATABASE_NAME = "K23411TSales.sqlite";
    public static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;
    private void copyDataBase(){
        try{
            File dbFile = getDatabasePath(DATABASE_NAME);
            if(!dbFile.exists()){
                if(CopyDBFromAsset()){
                    Toast.makeText(LoginActivity.this,
                            "Copy database successful!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(LoginActivity.this,
                            "Copy database fail!", Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e){
            Log.e("Error: ", e.toString());
        }
    }

    private boolean CopyDBFromAsset() {
        String dbPath = getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
        try {
            InputStream inputStream = getAssets().open(DATABASE_NAME);
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!f.exists()){
                f.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(dbPath);
            byte[] buffer = new byte[1024]; int length;
            while((length=inputStream.read(buffer))>0){
                outputStream.write(buffer,0, length);
            }
            outputStream.flush();  outputStream.close(); inputStream.close();
            return  true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        addViews ();
        copyDataBase();
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
        chkSaveInfor=findViewById(R.id.chkSaveInfor);
        radAdministrator=findViewById(R.id.radAdministrator);
        radEmployee=findViewById(R.id.radEmployee);
    }
    public void loginSystem(View view) {
        String username=edtUsername.getText().toString();
        String pw=edtPassword.getText().toString();
        UserAccount account= ListUserAccount.login(username,pw);
        if(account!=null)
        {
            SharedPreferences preferences=getSharedPreferences(name_share_ref,MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("UserName", username);
            editor.putString("Password", pw);
            boolean saved=chkSaveInfor.isChecked();
            editor.putBoolean("SAVED", saved);
            editor.commit();

            if(radAdministrator.isChecked())
            {
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USER_ACCOUNT", account);
                startActivity(intent);
            }
            else
            {
                Intent intent=new Intent(LoginActivity.this, EmployeeAdvancedMainActivity.class);
                startActivity(intent);
            }
            finish(); // Đóng màn hình Login sau khi chuyển trang thành công
            txtMessage.setText(R.string.str_login_success);
        }
        else
        {
            txtMessage.setText(R.string.str_login_failed);
        }
    }

    public void exitSystem(View view) {
        AlertDialog.Builder buider=new AlertDialog.Builder(LoginActivity.this);
        buider.setTitle(R.string.str_confirm_exit_title);
        buider.setMessage(R.string.str_confirm_exit_message);
        buider.setIcon(android.R.drawable.ic_dialog_alert);
        buider.setPositiveButton(R.string.str_confirm_exit_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        buider.setNegativeButton(R.string.str_confirm_exit_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog=buider.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences=getSharedPreferences(name_share_ref,MODE_PRIVATE);
        String username=preferences.getString("UserName", "");
        String password=preferences.getString("Password", "");
        boolean saved=preferences.getBoolean("SAVED", false);
        if(saved)
        {
            edtUsername.setText(username);
            edtPassword.setText(password);
        }
        chkSaveInfor.setChecked(saved);
    }
}