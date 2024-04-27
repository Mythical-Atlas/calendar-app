package com.example.calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MonthViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public final TextView dayOfMonth;
    public final TextView eventCount;
    private final MonthAdapter.OnItemListener onItemListener;

    public MonthViewHolder(@NonNull View itemView, MonthAdapter.OnItemListener onItemListener)
    {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.monthCellDayText);
        eventCount = itemView.findViewById(R.id.monthCellEventCountText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        onItemListener.onItemClick(getAdapterPosition(), (String)dayOfMonth.getText());
    }
}
