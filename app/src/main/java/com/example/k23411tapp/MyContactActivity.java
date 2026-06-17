package com.example.k23411tapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dals.MyContactDAO;
import com.example.models.MyContact;

import java.util.ArrayList;

public class MyContactActivity extends AppCompatActivity {
    ListView lvMyContact;
    ArrayList<MyContact> contacts;
    ArrayAdapter<MyContact> adapterMyContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_contact);
        addViews();
        loadMyContact();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        lvMyContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                MyContact contact=contacts.get(i);
                processDirectCall(contact);
            }
        });
    }

    private void processDirectCall(MyContact contact) {
        String phone=contact.getPhone();
        Intent intent= new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        startActivity(intent);
    }

    private void loadMyContact() {
        contacts.clear();
        ArrayList<MyContact> data = MyContactDAO.getMyContacts(this);
        contacts.addAll(data);
        adapterMyContact.notifyDataSetChanged();
    }

    private void addViews() {
        lvMyContact = findViewById(R.id.lvMyContact);
        contacts = MyContactDAO.getMyContacts(this);
        adapterMyContact = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        lvMyContact.setAdapter(adapterMyContact);
    }
}