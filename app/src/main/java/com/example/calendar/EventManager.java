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

    public static HashMap<LocalDate, ArrayList<EventObject>> eventList;
    public static ArrayList<EventObject> repeatingEventList;

    @SuppressWarnings("unchecked cast")
    public static void loadEvents(Context context){
        if(eventList == null)
        {
            eventList = new HashMap<LocalDate, ArrayList<EventObject>>();
        }
        eventList.clear();

        try
        {
            FileInputStream fis = context.openFileInput(eventListPath);
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

    private static void refreshRepeatingEvents()
    {
        if(repeatingEventList == null)
        {
            repeatingEventList = new ArrayList<EventObject>();
        }
        repeatingEventList.clear();

        for(ArrayList<EventObject> _eventList : eventList.values())
        {
            for(EventObject event : _eventList)
            {
                if (event.getRepeatType() != RepeatType.NONE)
                {
                    repeatingEventList.add(event);
                }
            }
        }
    }

    public static void wipeEventListAndStore(Context context)
    {
        eventList.clear();
        refreshRepeatingEvents();
        storeEvents(context);
    }

    public static ArrayList<EventObject> getEventsIncludingRepeats(LocalDate date)
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

    public static int getEventCountIncludingRepeats(LocalDate date)
    {
        return getEventsIncludingRepeats(date).size();
    }

    private static ArrayList<EventObject> getRepeatingEventsOnDate(LocalDate date)
    {
        ArrayList<EventObject> applicableEvents = new ArrayList<EventObject>();

        for(EventObject event : repeatingEventList)
        {
            if(doesAnEventRepeatOnDate(event, date)) {applicableEvents.add(event);}
        }

        return applicableEvents;
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

    public static void addEvent(EventObject event, Context context)
    {
        addEventWithoutStoring(event);

        // TODO: this could be optimized to only refresh the event we just added, but that also
        //       means handling any events that get modified
        refreshRepeatingEvents();
        storeEvents(context);
    }
    // TODO: verify that we're not adding an event with a duplicate uuid
    public static void addEventWithoutStoring(EventObject event) // TODO: verify that the pass-by-reference logic here is correct
    {
        LocalDate date = event.getDate();
        ArrayList<EventObject> eventsOnDate = null;

        if(eventList.containsKey(date))
        {
            // eventsOnDate could still be null, but eventList contains a reference
            eventsOnDate = eventList.get(date);
        }
        else
        {
            eventsOnDate = new ArrayList<EventObject>();
            eventList.put(date, eventsOnDate);
        }
        // eventList is verified to contain a reference to eventsOnDate at this point
        if(eventsOnDate == null)
        {
            eventsOnDate = new ArrayList<EventObject>();
        }

        eventsOnDate.add(event);
        if(event.getRepeatType() != RepeatType.NONE)
        {
            repeatingEventList.add(event);
        }
    }

    public static void modifyEvent(EventObject oldEvent, EventObject newEvent, Context context)
    {
        removeEventWithoutStoring(oldEvent);
        addEvent(newEvent, context);
    }

    public static EventObject findEvent(LocalDate date, String name) // TODO: fix usages so that we can remove this
    {
        ArrayList<EventObject> eventsOnDate = getEventsIncludingRepeats(date);
        for(EventObject event : eventsOnDate)
        {
            if(event.getName().equals(name)) {return event;}
        }
        return null;
    }

    // note that this will only work if the event passed in has the correct date
    // hopefully this never matters, because removing purely by UUID would be really slow
    public static void removeEvent(EventObject event, Context context)
    {
        removeEventWithoutStoring(event);
        storeEvents(context);
    }
    public static void removeEventWithoutStoring(EventObject event)
    {
        LocalDate date = event.getDate();
        if(eventList.containsKey(date))
        {
            ArrayList<EventObject> eventsOnDate = eventList.get(date);
            if(eventsOnDate == null) {return;}

            eventsOnDate.removeIf(e -> e.getUuid().equals(event.getUuid()));
            repeatingEventList.removeIf(e -> e.getUuid().equals(event.getUuid()));

            if(eventsOnDate.isEmpty())
            {
                eventList.remove(date);
            }
        }
    }
}
