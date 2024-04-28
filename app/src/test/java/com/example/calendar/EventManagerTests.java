package com.example.calendar;

import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class EventManagerTests {/*
    @Test
    public void addEvent_adds_event_to_date_in_list()
    {
        // arrange
        EventManager.eventList = new HashMap<LocalDate, ArrayList<EventObject>>();
        LocalDate date = LocalDate.now();
        EventObject event = new EventObject("doesn't matter", date, RepeatType.NONE, 0);

        // act
        EventManager.addEventWithoutStoring(event);

        // assert
        assertEquals(1, EventManager.eventList.size());
        assertTrue(EventManager.eventList.containsKey(date));
        assertTrue(EventManager.eventList.get(date).contains(event));
    }

    @Test
    public void removeEvent_removes_event_from_date_in_list()
    {
        // arrange
        EventManager.eventList = new HashMap<LocalDate, ArrayList<EventObject>>();
        EventManager.repeatingEventList = new ArrayList<EventObject>();
        LocalDate date = LocalDate.now();
        EventObject event1 = new EventObject("doesn't matter", date, RepeatType.NONE, 0);
        EventObject event2 = new EventObject("doesn't matter 2", date, RepeatType.NONE, 0);
        EventManager.addEventWithoutStoring(event1);
        EventManager.addEventWithoutStoring(event2);

        // act
        EventManager.removeEventWithoutStoring(event1);

        // assert
        assertEquals(1, EventManager.eventList.size());
        assertTrue(EventManager.eventList.containsKey(date));
        assertTrue(EventManager.eventList.get(date).contains(event2));
    }

    @Test
    public void removeEvent_removes_date_from_list_if_empty()
    {
        // arrange
        LocalDate date = LocalDate.now();
        UUID uuid = UUID.randomUUID();
        EventObject event = new EventObject(uuid, "doesn't matter", LocalDate.now(), RepeatType.NONE, 0);
        EventManager.eventList = new HashMap<LocalDate, ArrayList<EventObject>>();
        EventManager.repeatingEventList = new ArrayList<EventObject>();
        EventManager.addEventWithoutStoring(event);

        // act
        EventManager.removeEventWithoutStoring(event);

        // assert
        assertEquals(0, EventManager.eventList.size());
    }*/
}
