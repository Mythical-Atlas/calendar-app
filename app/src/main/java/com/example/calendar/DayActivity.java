package com.example.calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DayActivity extends AppCompatActivity implements DayAdapter.OnItemListener
{
    private TextView dayMonthYearText;
    private RecyclerView eventsRecyclerView;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        initWidgets();

        selectedDate = (LocalDate)getIntent().getSerializableExtra("date");
        setDayView();
    }

    private void initWidgets()
    {
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        dayMonthYearText = findViewById(R.id.dayMonthYearTV);
    }

    private void setDayView()
    {
        dayMonthYearText.setText(dayMonthYearFromDate(selectedDate));
        ArrayList<String> eventNames = getEventNames(selectedDate);

        DayAdapter dayAdapter = new DayAdapter(eventNames, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        eventsRecyclerView.setLayoutManager(layoutManager);
        eventsRecyclerView.setAdapter(dayAdapter);
    }

    private ArrayList<String> getEventNames(LocalDate date)
    {
        ArrayList<EventObject> events = MainActivity.eventManager.getEvents(selectedDate);
        ArrayList<String> eventNames = new ArrayList<>();

        if(events == null) {return eventNames;}

        for(EventObject event : events)
        {
            eventNames.add(event.getName());
        }

        return eventNames;
    }

    private String dayMonthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE',' MMMM dd',' yyyy");
        return date.format(formatter);
    }

    public void previousDayAction(View view)
    {
        selectedDate = selectedDate.minusDays(1);
        setDayView();
    }

    public void nextDayAction(View view)
    {
        selectedDate = selectedDate.plusDays(1);
        setDayView();
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        /*if(!dayText.equals(""))
        {
            String message = "Selected Event: " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }*/
    }
}
