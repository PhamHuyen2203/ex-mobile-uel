package com.example.k23411tapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FirebaseContactActivity extends AppCompatActivity {

    ListView lvContact;
    Button btnInsertContact;
    ArrayAdapter<String> contactAdapter;
    String TAG = "FIREBASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_firebase_contact);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        lvContact = findViewById(R.id.lvContact);
        btnInsertContact = findViewById(R.id.btnInsertContact);
        contactAdapter = new ArrayAdapter<>(FirebaseContactActivity.this, android.R.layout.simple_list_item_1);
        lvContact.setAdapter(contactAdapter);
        loadData();
    }

    private void addEvents() {
        btnInsertContact.setOnClickListener(v -> {
            Intent intent = new Intent(FirebaseContactActivity.this, InsertContactActivity.class);
            startActivity(intent);
        });

        lvContact.setOnItemClickListener((parent, view, position, id) -> {
            String data = contactAdapter.getItem(position);
            if (data != null) {
                String key = data.split("\n")[0];
                Intent intent = new Intent(FirebaseContactActivity.this, DetailContactActivity.class);
                intent.putExtra("KEY", key);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("contacts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactAdapter.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.getKey();
                    Object value = data.getValue();
                    if (value instanceof HashMap) {
                        HashMap<String, Object> map = (HashMap<String, Object>) value;
                        String name = map.containsKey("name") ? String.valueOf(map.get("name")) : "No Name";
                        String phone = map.containsKey("phone") ? String.valueOf(map.get("phone")) : "";
                        contactAdapter.add(key + "\n" + name + " - " + phone);
                    } else if (value != null) {
                        contactAdapter.add(key + "\n" + value.toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
