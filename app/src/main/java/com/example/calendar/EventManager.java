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

public class EventManager {
    private static HashMap<LocalDate, ArrayList<EventObject>> eventList;
    private static ArrayList<EventObject> repeatingEventList;

    public EventManager()
    {
        if(eventList == null)
        {
            eventList = new HashMap<LocalDate, ArrayList<EventObject>>();
        }

        if(repeatingEventList == null)
        {
            repeatingEventList = new ArrayList<EventObject>();
            refreshRepeatingEvents();
        }
    }

    public void deleteAllEvents()
    {
        eventList.clear();
        refreshRepeatingEvents();
    }

    public void loadEvents(Context context, String path){
        try
        {
            FileInputStream fis = context.openFileInput(path);
            ObjectInputStream oi = new ObjectInputStream(fis);
            eventList = (HashMap<LocalDate, ArrayList<EventObject>>)oi.readObject();
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

        refreshRepeatingEvents();
    }
    public void storeEvents(Context context, String path){
        try
        {
            FileOutputStream fos = context.openFileOutput(path, Context.MODE_PRIVATE);
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

    private void refreshRepeatingEvents()
    {
        repeatingEventList.clear();

        for(ArrayList<EventObject> _eventlist : eventList.values())
        {
            for(EventObject event : _eventlist)
            {
                if (event.getRepeatType() != EventObject.RepeatType.NONE)
                {
                    repeatingEventList.add(event);
                }
            }
        }
    }

    public ArrayList<EventObject> getEvents(LocalDate date)
    {
        ArrayList<EventObject> applicableEvents = getRepeatingEventsOnDate(date);

        if(eventList.containsKey(date))
        {
            if(eventList.get(date) != null)
            {
                applicableEvents.addAll(Objects.requireNonNull(eventList.get(date)));
            }
        }

        return applicableEvents;
    }

    private ArrayList<EventObject> getRepeatingEventsOnDate(LocalDate date)
    {
        ArrayList<EventObject> applicableEvents = new ArrayList<EventObject>();

        for(EventObject event : repeatingEventList)
        {
            if(doesEventRepeatOnDate(event, date)) {applicableEvents.add(event);}
        }

        return applicableEvents;
    }

    private boolean doesEventRepeatOnDate(EventObject event, LocalDate date)
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

    public void addEvent(LocalDate date, String name, EventObject.RepeatType repeatType)
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

            EventObject _event = new EventObject(date, name, repeatType);
            eventsOnDate.add(_event);

            if (_event.getRepeatType() != EventObject.RepeatType.NONE)
            {
                repeatingEventList.add(_event);
            }
        }
        else
        {
            ArrayList<EventObject> eventsOnDate = new ArrayList<EventObject>();
            EventObject _event = new EventObject(date, name, repeatType);
            eventsOnDate.add(_event);
            eventList.put(date, eventsOnDate);

            if (_event.getRepeatType() != EventObject.RepeatType.NONE)
            {
                repeatingEventList.add(_event);
            }
        }
    }

    public EventObject getEvent(LocalDate date, String name)
    {
        ArrayList<EventObject> eventsOnDate = new ArrayList<>();

        if(eventList.containsKey(date))
        {
            ArrayList<EventObject> _eventsOnDate = eventList.get(date);
            if(_eventsOnDate != null)
            {
                eventsOnDate.addAll(_eventsOnDate);
            }
        }

        eventsOnDate.addAll(getRepeatingEventsOnDate(date));

        for(EventObject event : eventsOnDate)
        {
            if(event.getName().equals(name)) {return event;}
        }

        return null;
    }

    public boolean checkDuplicate(LocalDate date, String name, EventObject.RepeatType repeatType)
    {
        ArrayList<EventObject> eventsOnDate = new ArrayList<>();

        if(eventList.containsKey(date))
        {
            ArrayList<EventObject> _eventsOnDate = eventList.get(date);
            if(_eventsOnDate != null)
            {
                eventsOnDate.addAll(_eventsOnDate);
            }
        }

        eventsOnDate.addAll(getRepeatingEventsOnDate(date));

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

            eventsOnDate.removeIf(e -> e.getName().equals(name) && e.getDate().equals(date));
            repeatingEventList.removeIf(e -> e.getName().equals(name) && e.getDate().equals(date));
        }
    }

    public int getEventCount(LocalDate date)
    {
        return getEvents(date).size();
    }
}
