package com.gregsprogrammingworks.timeclock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gregsprogrammingworks.timeclock.ui.main.EmployeeListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, EmployeeListFragment.newInstance())
                    .commitNow();
        }
    }
}