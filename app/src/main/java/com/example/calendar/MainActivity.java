package com.example.calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity
{
    public static LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        EventManager.loadEvents(this);

        selectedDate = LocalDate.now();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        navView.setSelectedItemId(R.id.botNavMonthView);

        findViewById(R.id.newEventButton).setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), ModifyEventActivity.class);
            intent.putExtra("new_event_mode", true);
            this.startActivity(intent);
        });
        findViewById(R.id.settingsButton).setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            this.startActivity(intent);
        });

        navView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.botNavDayView) {replaceFragment(new DayFragment());}
            if(item.getItemId() == R.id.botNavMonthView) {replaceFragment(new MonthFragment());}

            return true;
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        refreshFragment();
    }

    public void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

    public void refreshFragment()
    {
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        navView.setSelectedItemId(navView.getSelectedItemId());
    }
}