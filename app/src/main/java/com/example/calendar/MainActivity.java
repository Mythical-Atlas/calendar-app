package com.example.calendar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    public static LocalDate selectedDate;

    private NotificationChannel notificationChannel;
    private NotificationManager notificationManager;

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {});


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

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

        }
        else {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
        }


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
}