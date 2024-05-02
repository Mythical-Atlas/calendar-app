package com.example.calendar;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MonthAdapter extends RecyclerView.Adapter<MonthViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final ArrayList<String> eventCounts;
    private final OnItemListener onItemListener;

    public MonthAdapter(ArrayList<String> daysOfMonth, ArrayList<String> eventCounts, OnItemListener onItemListener)
    {
        this.daysOfMonth = daysOfMonth;
        this.eventCounts = eventCounts;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.month_cell, parent, false);

        int divHeightInDP = 5;
        int divHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, divHeightInDP + 0.5f, view.getResources().getDisplayMetrics());
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int)(parent.getHeight() * 0.166666666 - divHeight);

        return new MonthViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position)
    {
        holder.dayOfMonth.setText(daysOfMonth.get(position));
        holder.eventCount.setText(eventCounts.get(position));

        if(daysOfMonth.get(position).isEmpty())
        {
            holder.itemView.setBackground(holder.itemView.getResources().getDrawable(R.drawable.month_cell_background_empty));
        }

        if(eventCounts.get(position).isEmpty())
        {
            holder.eventCount.setBackground(null);
        }
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
