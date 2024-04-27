package com.example.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DayAdapter extends RecyclerView.Adapter<DayViewHolder>
{
    private final ArrayList<String> eventNames;
    private final OnItemListener onItemListener;

    public DayAdapter(ArrayList<String> eventNames, OnItemListener onItemListener)
    {
        this.eventNames = eventNames;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.day_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int)(parent.getHeight() * 0.166666666);
        return new DayViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position)
    {
        holder.eventName.setText(eventNames.get(position));
    }

    @Override
    public int getItemCount()
    {
        return eventNames.size();
    }

    public interface OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
