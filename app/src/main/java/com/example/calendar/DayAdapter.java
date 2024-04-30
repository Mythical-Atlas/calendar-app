package com.example.calendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.UUID;

public class DayAdapter extends RecyclerView.Adapter<DayViewHolder>
{
    private final ArrayList<UUID> eventUuids;
    private final OnItemListener onItemListener;

    public DayAdapter(ArrayList<UUID> eventUuids, OnItemListener onItemListener)
    {
        this.eventUuids = eventUuids;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.day_cell_expanded, parent, false);
        //ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        //layoutParams.height = 200;
        return new DayViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position)
    {
        UUID _uuid = eventUuids.get(position);
        EventObject _event = EventManager.getEvent(_uuid);

        holder.eventUuid = _uuid;
        holder.eventName.setText(_event.getName());

        int colorIndex = EventManager.getEvent(_uuid).getColor();
        if(colorIndex == 0)
        {
            holder.eventName.setBackgroundColor(Color.TRANSPARENT);
        }
        else
        {
            holder.eventName.setBackgroundColor(Color.parseColor(holder.itemView.getResources().getStringArray(R.array.color_value_array)[colorIndex]));
        }

        String repeatString = "Repeats ";
        RepeatType repeatType = _event.getRepeatType();
        switch(repeatType)
        {
            case NONE:
                repeatString = "Doesn't repeat.";
                break;
            case DAILY:
                repeatString += "daily";
                break;
            case WEEKLY:
                repeatString += "weekly";
                break;
            case MONTHLY:
                repeatString += "monthly";
                break;
            case YEARLY:
                repeatString += "yearly";
                break;
        }
        if(repeatType != RepeatType.NONE)
        {
            repeatString += ", " + _event.getRepeatTimes() + " times.";
        }
        holder.repeatTextView.setText(repeatString);

        holder.locationTextView.setText(_event.getLocation());
        holder.tempColorTextView.setText(String.valueOf(_event.getColor()));
    }

    @Override
    public int getItemCount()
    {
        return eventUuids.size();
    }

    public interface OnItemListener
    {
        void onEventExpand(int position);
        void onEventModify(UUID eventUuid);
    }
}
