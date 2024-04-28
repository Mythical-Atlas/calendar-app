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

        EventAddModifyStatus status = MainActivity.eventManager.modifyEvent(event.getUuid(), eventDate, eventName, repeatType);

        switch(status)
        {
            case SUCCESS:
                MainActivity.storeEventList(getApplicationContext());
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
            case FAILED_BECAUSE_INVALID_DATE:
                Toast.makeText(this, "Somehow, the date for this event was invalid. You should never see this message...", Toast.LENGTH_SHORT).show();
                return;
            case FAILED_BECAUSE_INVALID_NAME:
                Toast.makeText(this, "Please enter a name for this event.", Toast.LENGTH_SHORT).show();
                return;
            case FAILED_BECAUSE_INVALID_REPEAT:
                Toast.makeText(this, "Somehow, the repeat type for this event was invalid. You should never see this message...", Toast.LENGTH_SHORT).show();
                return;
            case FAILED_BECAUSE_CLASH:
                Toast.makeText(this, "This event clashes with an existing event.", Toast.LENGTH_SHORT).show();
                return;
        }
    }
}