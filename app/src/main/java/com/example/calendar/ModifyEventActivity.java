package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

public class ModifyEventActivity extends AppCompatActivity
{
    private TextView pageTitle;
    private TextView eventNameEditText;
    private Spinner repeatTypeSpinner;
    private CalendarView calendarView;
    private Button deleteEventButton;
    private Button saveChangesButton;

    private boolean newEventMode;
    private UUID eventUuid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);
        getWidgets();

        newEventMode = getIntent().getBooleanExtra("new_event_mode", false);

        if(newEventMode)
        {
            pageTitle.setText("Create a New Event");
            saveChangesButton.setText("Create Event");
            deleteEventButton.setVisibility(View.INVISIBLE);

            calendarView.setDate(MainActivity.selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        else
        {
            pageTitle.setText("Modify an Existing Event");
            saveChangesButton.setText("Save Changes");
            deleteEventButton.setVisibility(View.VISIBLE);

            eventUuid = (UUID)getIntent().getSerializableExtra("event_to_modify");
            EventObject event = EventManager.getEvent(eventUuid);

            eventNameEditText.setText(event.getName());
            calendarView.setDate(event.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
            repeatTypeSpinner.setSelection(event.getRepeatType().ordinal());
        }

        findViewById(R.id.cancelEventCreationButton).setOnClickListener(l ->
        {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        });
        saveChangesButton.setOnClickListener(l -> trySaveEvent());
        deleteEventButton.setOnClickListener(l ->
        {
            EventManager.deleteEvent(eventUuid);
            EventManager.storeEvents(this);

            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        });

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) ->
        {
            view.setDate(LocalDate.of(year, month + 1, dayOfMonth).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        });
    }

    private void getWidgets()
    {
        pageTitle = findViewById(R.id.pageTitle);
        calendarView = findViewById(R.id.calendarView);
        eventNameEditText = findViewById(R.id.eventNameEditText);
        repeatTypeSpinner = findViewById(R.id.repeat_type_spinner);
        deleteEventButton = findViewById(R.id.deleteEventButton);
        saveChangesButton = findViewById(R.id.saveChangesButton);
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

        if(newEventMode)
        {
            EventManager.addEvent(new EventObject(eventName, eventDate, repeatType));
        }
        else
        {
            EventManager.setEventName(eventUuid, eventName);
            EventManager.setEventDate(eventUuid, eventDate);
            EventManager.setEventRepeatType(eventUuid, repeatType);
        }
        EventManager.storeEvents(this);

        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}