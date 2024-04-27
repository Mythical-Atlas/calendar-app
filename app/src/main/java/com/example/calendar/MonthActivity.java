package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MonthActivity extends AppCompatActivity implements MonthAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    public static EventManager eventManager = new EventManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // for debug
        eventManager.addEvent(LocalDate.of(2024, 4, 26), "Test Event");
        eventManager.addEvent(LocalDate.of(2024, 4, 26), "Test Event2");
        eventManager.addEvent(LocalDate.of(2024, 4, 26), "Test Event3");
        eventManager.addEvent(LocalDate.of(2024, 4, 26), "Test Event4");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        ArrayList<String> eventCountsInMonth = eventCountsInMonthArray(selectedDate);

        MonthAdapter monthAdapter = new MonthAdapter(daysInMonth, eventCountsInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(monthAdapter);
    }

    private ArrayList<String> eventCountsInMonthArray(LocalDate date)
    {
        ArrayList<String> eventCountsInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = date.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                eventCountsInMonthArray.add("");
            }
            else
            {
                LocalDate key = firstOfMonth.plusDays(i - dayOfWeek - 1);
                int eventCount = eventManager.getEventCount(key);

                if(eventCount == 0)
                {
                    eventCountsInMonthArray.add("");
                }
                else
                {
                    eventCountsInMonthArray.add(String.valueOf(eventCount));
                }
            }
        }

        return eventCountsInMonthArray;
    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = date.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }

        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        Intent intent = new Intent(this, DayActivity.class);
        intent.putExtra("date", getDateFromPosition(position));
        this.startActivity(intent);

        /*if(!dayText.equals(""))
        {
            String message = "Selected Date: " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }*/
    }

    private LocalDate getDateFromPosition(int position)
    {
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        return firstOfMonth.plusDays(position - dayOfWeek);
    }
}