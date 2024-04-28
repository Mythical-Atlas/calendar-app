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

    public EventObject(String name, LocalDate date, RepeatType repeatType, int weekdayBits) {
        uuid = UUID.randomUUID();
        this.name = name;
        this.date = date;
        this.repeatType = repeatType;
        this.weekdayBits = weekdayBits;
    }
    public EventObject(UUID uuid, String name, LocalDate date, RepeatType repeatType, int weekdayBits) {
        this.uuid = uuid;
        this.name = name;
        this.date = date;
        this.repeatType = repeatType;
        this.weekdayBits = weekdayBits;
    }

    public UUID getUuid() {return uuid;}
    public String getName() {return name;}
    public LocalDate getDate() {return date;}
    public RepeatType getRepeatType() {return repeatType;}
    public int getWeekdayBits() {return weekdayBits;}
}
