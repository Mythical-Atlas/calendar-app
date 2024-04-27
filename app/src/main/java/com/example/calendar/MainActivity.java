package com.example.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

    private NotificationChannel notificationChannel;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // for debug
        /*eventManager.addEvent(LocalDate.of(2024, 4, 26), "Test Event");
        eventManager.addEvent(LocalDate.of(2024, 4, 26), "Test Event2");
        eventManager.addEvent(LocalDate.of(2024, 4, 26), "Test Event3");
        eventManager.addEvent(LocalDate.of(2024, 4, 26), "Test Event4");

        eventManager.storeEvents(this, "calendar_event_list");*/

        eventManager.loadEvents(this, "calendar_event_list");

        selectedDate = LocalDate.now();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        navView.setSelectedItemId(R.id.botNavMonthView);

        findViewById(R.id.newEventButton).setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), NewEventActivity.class);
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

        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setSmallIcon(R.drawable.baseline_event_24)
                .setContentTitle("Calendar Event")
                .setContentText("Lorem Ipsum test event! Hello world!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        int temp_id = 298;
        notificationManager.notify(temp_id, builder.build());
    }

    private void createNotificationChannel() {
        CharSequence name = "channel_name";
        String description = "channel_description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(getString(R.string.notification_channel_id), name, importance);
        channel.setDescription(description);
        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
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

    public static void storeEventList(Context context)
    {
        eventManager.storeEvents(context, "calendar_event_list");
    }

    public static void deleteAllEvents(Context context)
    {
        eventManager.deleteAllEvents();
        eventManager.storeEvents(context, "calendar_event_list");
    }
}