package com.example.kshitij.patentlite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class Navigation2Activity extends AppCompatActivity {

    private ViewPager viewPager;
    public int role;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle("Applications");
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    setTitle("Status");
                    viewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation2);
        setTitle("Applications");
        role = getIntent().getExtras().getInt("role");
        BottomNavigationView navigation = findViewById(R.id.navigation2);
        viewPager = findViewById(R.id.viewpager2);

        // Create an adapter that knows which fragment should be shown on each page
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.setRole(role);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
