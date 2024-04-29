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
    private int weekdayBits;
    private int repeatTimes;
    private UUID classUuid;
    private int color; // TODO: figure out the best datatype for this
    private String notes;
    private String location;
    private int reminderOffset; // TODO: figure out the best datatype for this AND a better name

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
    public int getWeekdayBits() {return weekdayBits;}
    public int getRepeatTimes() {return repeatTimes;}
    public UUID getClassUuid() {return classUuid;}
    public int getColor() {return color;}
    public String getNotes() {return notes;}
    public String getLocation() {return location;}
    public int getReminderOffset() {return reminderOffset;}

    public void setName(String name) {this.name = name;}
    public void setDate(LocalDate date) {this.date = date;}
    public void setRepeatType(RepeatType repeatType) {this.repeatType = repeatType;}
    public void setWeekdayBits(int weekdayBits) {this.weekdayBits = weekdayBits;}
    public void setRepeatTimes(int repeatTimes) {this.repeatTimes = repeatTimes;}
    public void setClassUuid(UUID classUuid) {this.classUuid = classUuid;}
    public void setColor(int color) {this.color = color;}
    public void setNotes(String notes) {this.notes = notes;}
    public void setLocation(String location) {this.location = location;}
    public void setReminderOffset(int reminderOffset) {this.reminderOffset = reminderOffset;}
}