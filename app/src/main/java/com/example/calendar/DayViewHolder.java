package com.example.calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public final TextView eventName;
    private final DayAdapter.OnItemListener onItemListener;

    public DayViewHolder(@NonNull View itemView, DayAdapter.OnItemListener onItemListener)
    {
        super(itemView);
        eventName = itemView.findViewById(R.id.dayCellEventNameText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        onItemListener.onItemClick(getAdapterPosition(), (String)eventName.getText());
    }
}
