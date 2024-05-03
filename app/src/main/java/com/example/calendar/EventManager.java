package com.example.calendar;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class EventManager {
    private static final String eventListPath = "calendar_event_list";

    public static HashMap<UUID, EventObject> eventList;

    @SuppressWarnings("unchecked cast")
    public static void loadEvents(Context context){
        if(eventList == null)
        {
            eventList = new HashMap<UUID, EventObject>();
        }
        eventList.clear();

        try
        {
            FileInputStream fis = context.openFileInput(eventListPath);
            ObjectInputStream oi = new ObjectInputStream(fis);
            eventList = (HashMap<UUID, EventObject>)oi.readObject();
            oi.close();
            fis.close();
        }
        catch (ClassNotFoundException e)
        {
            Log.e("EventManager.loadEvents", "Class of serialized object not found.");
            throw new RuntimeException(e);
        }
        catch (FileNotFoundException e)
        {
            Log.e("EventManager.loadEvents", "File not found.");
            //throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            Log.e("EventManager.loadEvents", "Error initializing stream.");
            //throw new RuntimeException(e);
        }
    }
    public static void storeEvents(Context context){
        try
        {
            FileOutputStream fos = context.openFileOutput(eventListPath, Context.MODE_PRIVATE);
            ObjectOutputStream oo = new ObjectOutputStream(fos);
            oo.writeObject(eventList);
            oo.close();
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            Log.e("EventManager.loadEvents", "File not found.");
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            Log.e("EventManager.loadEvents", "Error initializing stream.");
            throw new RuntimeException(e);
        }
    }

    public static void clearEvents()
    {
        eventList.clear();
    }

    public static ArrayList<EventObject> getEventsIncludingRepeats(LocalDate date)
    {
        ArrayList<EventObject> applicableEvents = new ArrayList<EventObject>();

        for(EventObject event : eventList.values())
        {
            if(event.getDate().equals(date) || doesAnEventRepeatOnDate(event, date)) {applicableEvents.add(event);}
        }

        return applicableEvents;
    }

    public static EventObject getEvent(UUID uuid)
    {
        return eventList.get(uuid);
    }

    public static int getEventCountIncludingRepeats(LocalDate date)
    {
        return getEventsIncludingRepeats(date).size();
    }

    private static boolean doesAnEventRepeatOnDate(EventObject event, LocalDate date)
    {
        switch(event.getRepeatType())
        {
            case NONE:
                return false;
            case DAILY:
                return date.isAfter(event.getDate());
            case WEEKLY:
                if(!date.isAfter(event.getDate())) {return false;}
                return event.getDate().getDayOfWeek() == date.getDayOfWeek();
            case MONTHLY:
                if(!date.isAfter(event.getDate())) {return false;}
                return event.getDate().getDayOfMonth() == date.getDayOfMonth();
            case YEARLY:
                if(!date.isAfter(event.getDate())) {return false;}
                return event.getDate().getDayOfYear() == date.getDayOfYear();
            default:
                return false;
        }
    }

    public static void addEvent(EventObject event)
    {
        eventList.put(event.getUuid(), event);
    }

    public static void deleteEvent(UUID uuid)
    {
        eventList.remove(uuid);
    }

    public static void setEventName(UUID uuid, String name)
    {
        EventObject _event = getEvent(uuid);
        _event.setName(name);
    }
    public static void setEventDate(UUID uuid, LocalDate date)
    {
        EventObject _event = getEvent(uuid);
        _event.setDate(date);
    }
    public static void setEventRepeatType(UUID uuid, RepeatType repeatType)
    {
        EventObject _event = getEvent(uuid);
        _event.setRepeatType(repeatType);
    }
    public static void setEventRepeatTimes(UUID uuid, int repeatTimes)
    {
        EventObject _event = getEvent(uuid);
        _event.setRepeatTimes(repeatTimes);
    }
    public static void setEventColor(UUID uuid, int color)
    {
        EventObject _event = getEvent(uuid);
        _event.setColor(color);
    }
    public static void setEventLocation(UUID uuid, String location)
    {
        EventObject _event = getEvent(uuid);
        _event.setLocation(location);
    }
}

