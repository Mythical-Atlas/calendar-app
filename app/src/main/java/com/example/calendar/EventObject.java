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
    private LocalDate date;
    private String name;
    private RepeatType repeatType;

    public EventObject(LocalDate date, String name, RepeatType repeatType) {
        uuid = UUID.randomUUID();
        this.date = date;
        this.name = name;
        this.repeatType = repeatType;
    }
    public EventObject(UUID uuid, LocalDate date, String name, RepeatType repeatType) {
        this.uuid = uuid;
        this.date = date;
        this.name = name;
        this.repeatType = repeatType;
    }

    public UUID getUuid() {return uuid;}
    public LocalDate getDate() {return date;}
    public String getName() {return name;}
    public RepeatType getRepeatType() {return repeatType;}
}
