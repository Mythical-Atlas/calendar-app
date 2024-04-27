package com.example.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public static EventManager eventManager = new EventManager();
    public static LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // for debug
        eventManager.addEvent(LocalDate.of(2024, 4, 26), "Test Event");
        eventManager.addEvent(LocalDate.of(2024, 4, 26), "Test Event2");
        eventManager.addEvent(LocalDate.of(2024, 4, 26), "Test Event3");
        eventManager.addEvent(LocalDate.of(2024, 4, 26), "Test Event4");

        selectedDate = LocalDate.now();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replaceFragment(new MonthFragment());

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        navView.setSelectedItemId(R.id.botNavMonthView);

        navView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.botNavDayView) {replaceFragment(new DayFragment());}
            if(item.getItemId() == R.id.botNavMonthView) {replaceFragment(new MonthFragment());}

            return true;
        });
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }
}