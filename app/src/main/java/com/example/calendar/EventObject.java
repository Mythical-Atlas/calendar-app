package com.example.calendar;

import java.io.Serializable;
import java.time.LocalDate;

public class EventObject implements Serializable {
    enum RepeatType
    {
        NONE,
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

    private LocalDate date;
    private String name;
    private RepeatType repeatType;

    public EventObject(LocalDate date, String name) {
        this.date = date;
        this.name = name;
        repeatType = RepeatType.NONE;
    }
    public EventObject(LocalDate date, String name, RepeatType repeatType) {
        this.date = date;
        this.name = name;
        this.repeatType = repeatType;
    }

    public LocalDate getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public RepeatType getRepeatType() {return repeatType;}
    public void setRepeatType(RepeatType repeatType) {this.repeatType = repeatType;}
}
