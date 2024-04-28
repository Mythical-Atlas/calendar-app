package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class ModifyEventActivity extends AppCompatActivity
{
    private TextView eventNameEditText;
    private Spinner repeatTypeSpinner;
    private CalendarView calendarView;
    private EventObject event;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);
        getWidgets();

        event = (EventObject)getIntent().getSerializableExtra("event_to_modify");

        eventNameEditText.setText(event.getName());
        calendarView.setDate(event.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        repeatTypeSpinner.setSelection(event.getRepeatType().ordinal());

        findViewById(R.id.cancelEventCreationButton).setOnClickListener(l ->
        {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        });
        findViewById(R.id.createEventButton).setOnClickListener(l -> trySaveEvent());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) ->
        {
            view.setDate(LocalDate.of(year, month + 1, dayOfMonth).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        });
    }

    private void getWidgets()
    {
        calendarView = findViewById(R.id.calendarView);
        eventNameEditText = findViewById(R.id.eventNameEditText);
        repeatTypeSpinner = findViewById(R.id.repeat_type_spinner);
    }

    private void trySaveEvent()
    {
        String eventName = eventNameEditText.getText().toString();
        LocalDate eventDate = Instant.ofEpochMilli(calendarView.getDate()).atZone(ZoneId.systemDefault()).toLocalDate();
        int repeatIndex = repeatTypeSpinner.getSelectedItemPosition();
        RepeatType repeatType = RepeatType.values()[repeatIndex];

        if(eventName.isEmpty())
        {
            Toast.makeText(this, "Please enter a name for this event.", Toast.LENGTH_SHORT).show();
            return;
        }

        EventManager.modifyEvent(event, new EventObject(eventName, eventDate, repeatType, 0), getApplicationContext());
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}