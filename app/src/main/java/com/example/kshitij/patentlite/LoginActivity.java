package com.example.kshitij.patentlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private int role = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button admin = findViewById(R.id.admin);
        Button appraiser = findViewById(R.id.appraiser);
        Button applicant = findViewById(R.id.applicant);
        Button inspector = findViewById(R.id.inspector);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role = 4;
                redirect2();
            }
        });
        appraiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role = 2;
                redirect2();
            }
        });
        applicant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role = 1;
                redirect();
            }
        });
        inspector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role = 3;
                redirect2();
            }
        });
    }
    public void redirect() {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra("role", role);
        startActivity(intent);
    }

    public void redirect2() {
        Intent intent = new Intent(this, Navigation2Activity.class);
        intent.putExtra("role", role);
        startActivity(intent);
    }
}
