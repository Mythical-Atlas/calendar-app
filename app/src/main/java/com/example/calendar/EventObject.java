package com.example.calendar;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

enum RepeatType
{
    NONE,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}

public class EventObject implements Serializable {
    private UUID uuid;
    private String name;
    private LocalDate date;
    private RepeatType repeatType;
    private int repeatTimes;
    private int color; // TODO: figure out the best datatype for this
    private String location;

    public EventObject(
            String name,
            LocalDate date,
            RepeatType repeatType
    ) {
        uuid = UUID.randomUUID();
        this.name = name;
        this.date = date;
        this.repeatType = repeatType;
    }

    public UUID getUuid() {return uuid;}
    public String getName() {return name;}
    public LocalDate getDate() {return date;}
    public RepeatType getRepeatType() {return repeatType;}
    public int getRepeatTimes() {return repeatTimes;}
    public int getColor() {return color;}
    public String getLocation() {return location;}

    public void setName(String name) {this.name = name;}
    public void setDate(LocalDate date) {this.date = date;}
    public void setRepeatType(RepeatType repeatType) {this.repeatType = repeatType;}
    public void setRepeatTimes(int repeatTimes) {this.repeatTimes = repeatTimes;}
    public void setColor(int color) {this.color = color;}
    public void setLocation(String location) {this.location = location;}
}