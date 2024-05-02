package com.example.calendar;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.UUID;

public class DayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView eventName;
    public TextView repeatTextView;
    public TextView locationTextView;
    public ConstraintLayout colorRing;

    private Button modifyButton;

    public UUID eventUuid;
    private final DayAdapter.OnItemListener onItemListener;

    private int normalHeight;
    private int noLocationHeight;
    private int expandedHeight;
    public boolean expanded;

    public DayViewHolder(@NonNull View itemView, DayAdapter.OnItemListener onItemListener)
    {
        super(itemView);
        initWidgets();
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);

        repeatTextView.setVisibility(View.INVISIBLE);
        locationTextView.setVisibility(View.INVISIBLE);
        modifyButton.setVisibility(View.INVISIBLE);

        modifyButton.setOnClickListener(l -> onItemListener.onEventModify(eventUuid));

        expandedHeight = itemView.getLayoutParams().height;
        int offsetInDP = 40;
        noLocationHeight = expandedHeight -(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, offsetInDP + 0.5f, itemView.getResources().getDisplayMetrics());
        int heightInDP = 60;
        normalHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightInDP + 0.5f, itemView.getResources().getDisplayMetrics());

        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.height = normalHeight;
        this.itemView.setLayoutParams(layoutParams);

        expanded = false;
    }

    private void initWidgets()
    {
        eventName = itemView.findViewById(R.id.dayCellEventNameText);
        repeatTextView = itemView.findViewById(R.id.repeatTextView);
        locationTextView = itemView.findViewById(R.id.locationTextView);
        modifyButton = itemView.findViewById(R.id.modifyButton);
        colorRing = itemView.findViewById(R.id.colorRingLayout);
    }

    @Override
    public void onClick(View v)
    {
        onItemListener.onEventExpand(getAdapterPosition());
    }

    public void expand()
    {
        if(!expanded)
        {
            expanded = true;
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            if(!locationTextView.getText().toString().isEmpty())
            {
                layoutParams.height = expandedHeight;
            }
            else
            {
                layoutParams.height = noLocationHeight;
            }
            this.itemView.setLayoutParams(layoutParams);

            repeatTextView.setVisibility(View.VISIBLE);
            locationTextView.setVisibility(View.VISIBLE);
            modifyButton.setVisibility(View.VISIBLE);
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

            repeatTextView.setVisibility(View.INVISIBLE);
            locationTextView.setVisibility(View.INVISIBLE);
            modifyButton.setVisibility(View.INVISIBLE);
        }
    }
}
