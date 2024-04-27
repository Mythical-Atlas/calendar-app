package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class NewEventActivity extends AppCompatActivity
{
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        calendarView = findViewById(R.id.calendarView);

        calendarView.setDate(MainActivity.selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());

        findViewById(R.id.cancelEventCreationButton).setOnClickListener(l ->
        {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        });
        findViewById(R.id.createEventButton).setOnClickListener(l -> tryCreateEvent());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) ->
        {
            view.setDate(LocalDate.of(year, month + 1, dayOfMonth).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        });
    }

    private void tryCreateEvent()
    {
        String eventName = ((TextView)findViewById(R.id.eventNameEditText)).getText().toString();
        LocalDate eventDate = Instant.ofEpochMilli(calendarView.getDate()).atZone(ZoneId.systemDefault()).toLocalDate();

        if(eventName.equals(""))
        {
            Toast t = Toast.makeText(this, "Please enter a name for this event.", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        if(MainActivity.eventManager.checkDuplicate(eventDate, eventName))
        {
            Toast t = Toast.makeText(this, "This event is a duplicate of an existing event.", Toast.LENGTH_SHORT);
            t.show();
            return;
        }

        MainActivity.eventManager.addEvent(eventDate, eventName);

        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}