package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MonthFragment extends Fragment implements MonthAdapter.OnItemListener, GestureDetector.OnGestureListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private GestureDetectorCompat gestureDetector;

    public MonthFragment() {}

    public static MonthFragment newInstance() {
        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_month, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.monthBackButton).setOnClickListener(v -> previousMonthAction(view));
        view.findViewById(R.id.monthForwardButton).setOnClickListener(v -> nextMonthAction(view));

        gestureDetector = new GestureDetectorCompat(view.getContext(),this);

        view.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        initWidgets(view);
        setMonthView();
    }

    private void initWidgets(View view)
    {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(MainActivity.selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(MainActivity.selectedDate);
        ArrayList<String> eventCountsInMonth = eventCountsInMonthArray(MainActivity.selectedDate);

        MonthAdapter monthAdapter = new MonthAdapter(daysInMonth, eventCountsInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
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
                int eventCount = MainActivity.eventManager.getEventCount(key);

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

    public void previousMonthAction(View view) {
        MainActivity.selectedDate = MainActivity.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        MainActivity.selectedDate = MainActivity.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            MainActivity.selectedDate = getDateFromPosition(position);
            ((BottomNavigationView)getActivity().findViewById(R.id.bottomNavigationView)).setSelectedItemId(R.id.botNavDayView);
        }

        //Intent intent = new Intent(this, DayActivity.class);
        //intent.putExtra("date", getDateFromPosition(position));
        //this.startActivity(intent);

        /*if(!dayText.equals(""))
        {
            String message = "Selected Date: " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }*/
    }

    private LocalDate getDateFromPosition(int position)
    {
        LocalDate firstOfMonth = MainActivity.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        return firstOfMonth.plusDays(position - dayOfWeek);
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        Log.i("motion", "down");

        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        Log.i("motion", "fling");

        return false;
    }
}