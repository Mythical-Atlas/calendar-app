package com.example.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class EventManager {
    private static HashMap<LocalDate, ArrayList<EventObject>> eventList;

    public EventManager()
    {
        if(eventList == null)
        {
            eventList = new HashMap<LocalDate, ArrayList<EventObject>>();
        }
    }
    public EventManager(HashMap<LocalDate, ArrayList<EventObject>> eventList)
    {
        this.eventList = eventList;
    }

    public HashMap<LocalDate, ArrayList<EventObject>> getEventList() {return eventList;}
    public void setEventList(HashMap<LocalDate, ArrayList<EventObject>> eventList) {this.eventList = eventList;}

    public ArrayList<EventObject> getEvents(LocalDate date)
    {
        if(eventList.containsKey(date))
        {
            return eventList.get(date);
        }

        return null;
    }

    public void addEvent(LocalDate date, String name)
    {
        if(eventList.containsKey(date))
        {
            ArrayList<EventObject> eventsOnDate = eventList.get(date);

            if(eventsOnDate == null)
            {
                eventsOnDate = new ArrayList<EventObject>();
            }

            // ensure no duplicate names on a date
            for(EventObject event : eventsOnDate)
            {
                if(event.getName().equals(name)) {return;}
            }

            eventsOnDate.add(new EventObject(date, name));
        }
        else
        {
            ArrayList<EventObject> eventsOnDate = new ArrayList<EventObject>();
            eventsOnDate.add(new EventObject(date, name));
            eventList.put(date, eventsOnDate);
        }
    }

    public EventObject getEvent(LocalDate date, String name)
    {
        if(eventList.containsKey(date))
        {
            ArrayList<EventObject> eventsOnDate = eventList.get(date);

            if(eventsOnDate == null) {return null;}

            for(EventObject event : eventsOnDate)
            {
                if(event.getName().equals(name)) {return event;}
            }
        }

        return null;
    }

    public boolean checkDuplicate(LocalDate date, String name)
    {
        if(!eventList.containsKey(date)) {return false;}

        ArrayList<EventObject> eventsOnDate = eventList.get(date);
        if(eventsOnDate == null) {return false;}

        for(EventObject event : eventsOnDate)
        {
            if(event.getName().equals(name)) {return true;}
        }

        return false;
    }

    public void removeEvent(LocalDate date, String name)
    {
        if(eventList.containsKey(date))
        {
            ArrayList<EventObject> eventsOnDate = eventList.get(date);

            if(eventsOnDate == null) {return;}

            eventsOnDate.removeIf(event -> event.getName().equals(name));
        }
    }

    public int getEventCount(LocalDate date)
    {
        if(eventList.containsKey(date))
        {
            ArrayList<EventObject> eventsOnDate = eventList.get(date);

            if(eventsOnDate == null) {return 0;}

            return eventsOnDate.size();
        }

        return 0;
    }
}
