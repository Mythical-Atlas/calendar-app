package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DayFragment extends Fragment implements DayAdapter.OnItemListener
{
    private TextView dayMonthYearText;
    private RecyclerView eventsRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.dayBackButton).setOnClickListener(v -> previousDayAction(view));
        view.findViewById(R.id.dayForwardButton).setOnClickListener(v -> nextDayAction(view));

        initWidgets(view);
        setDayView();
    }

    private void initWidgets(View view)
    {
        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        dayMonthYearText = view.findViewById(R.id.dayMonthYearTV);
    }

    private void setDayView()
    {
        dayMonthYearText.setText(dayMonthYearFromDate(MainActivity.selectedDate));
        ArrayList<String> eventNames = getEventNames(MainActivity.selectedDate);

        DayAdapter dayAdapter = new DayAdapter(eventNames, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        eventsRecyclerView.setLayoutManager(layoutManager);
        eventsRecyclerView.setAdapter(dayAdapter);
    }

    private ArrayList<String> getEventNames(LocalDate date)
    {
        ArrayList<EventObject> events = MainActivity.eventManager.getEvents(MainActivity.selectedDate);
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
        MainActivity.selectedDate = MainActivity.selectedDate.minusDays(1);
        setDayView();
    }

    public void nextDayAction(View view)
    {
        MainActivity.selectedDate = MainActivity.selectedDate.plusDays(1);
        setDayView();
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            Intent intent = new Intent(getContext(), ModifyEventActivity.class);
            intent.putExtra("event_to_modify", MainActivity.eventManager.getEvent(MainActivity.selectedDate, dayText));
            this.startActivity(intent);
        }
        /*if(!dayText.equals(""))
        {
            String message = "Selected Event: " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }*/
    }
}