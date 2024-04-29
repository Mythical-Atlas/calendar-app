package com.example.calendar;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.UUID;

public class DayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public final TextView eventName;
    private Button leftBut;
    private Button rightBut;

    public UUID eventUuid;
    private final DayAdapter.OnItemListener onItemListener;

    private int normalHeight;
    private int expandedHeight;
    public boolean expanded;

    public DayViewHolder(@NonNull View itemView, DayAdapter.OnItemListener onItemListener)
    {
        super(itemView);
        eventName = itemView.findViewById(R.id.dayCellEventNameText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);

        leftBut = itemView.findViewById(R.id.button);
        rightBut = itemView.findViewById(R.id.button2);

        normalHeight = itemView.getLayoutParams().height;
        expandedHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150 + 0.5f, itemView.getResources().getDisplayMetrics());

        expanded = false;
        leftBut.setVisibility(View.INVISIBLE);
        rightBut.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v)
    {
        onItemListener.onItemClick(getAdapterPosition(), eventUuid);
    }

    public void expand()
    {
        if(!expanded)
        {
            expanded = true;
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.height = expandedHeight;
            this.itemView.setLayoutParams(layoutParams);

            leftBut.setVisibility(View.VISIBLE);
            rightBut.setVisibility(View.VISIBLE);
        }
    }
    public void shrink()
    {
        if(expanded)
        {
            expanded = false;
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.height = normalHeight;
            this.itemView.setLayoutParams(layoutParams);

            leftBut.setVisibility(View.INVISIBLE);
            rightBut.setVisibility(View.INVISIBLE);
        }
    }
}
