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

enum EventAddModifyStatus
{
    SUCCESS,
    FAILED_BECAUSE_INVALID_DATE,
    FAILED_BECAUSE_INVALID_NAME,
    FAILED_BECAUSE_INVALID_REPEAT,
    FAILED_BECAUSE_CLASH,
    ERROR
}

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
                if (event.getRepeatType() != RepeatType.NONE)
                {
                    repeatingEventList.add(event);
                }
            }
        }
    }

    public ArrayList<EventObject> getEventsIncludingRepeats(LocalDate date)
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
            if(doesAnEventRepeatOnDate(event, date)) {applicableEvents.add(event);}
        }

        return applicableEvents;
    }

    private boolean doesAnEventRepeatOnDate(EventObject event, LocalDate date)
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

    public EventAddModifyStatus addEvent(LocalDate date, String name, RepeatType repeatType)
    {
        if(eventList.containsKey(date))
        {
            ArrayList<EventObject> eventsOnDate = eventList.get(date);

            if(eventsOnDate == null)
            {
                eventsOnDate = new ArrayList<EventObject>();
            }

            for(EventObject event : eventsOnDate)
            {
                if(event.getName().equals(name)) {return;}
            }

            EventObject _event = new EventObject(date, name, repeatType);
            eventsOnDate.add(_event);

            if (_event.getRepeatType() != RepeatType.NONE)
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

            if (_event.getRepeatType() != RepeatType.NONE)
            {
                repeatingEventList.add(_event);
            }
        }
    }

    public EventAddModifyStatus modifyEvent(UUID eventUuid, LocalDate newDate, String newName, RepeatType newRepeatType)
    {

    }

    private EventObject findEvent(UUID uuid) // TODO: is this necessary?
    {

        return null;
    }

    public EventObject findEvent(LocalDate date, String name)
    {
        ArrayList<EventObject> eventsOnDate = getEventsIncludingRepeats(date);
        for(EventObject event : eventsOnDate)
        {
            if(event.getName().equals(name)) {return event;}
        }
        return null;
    }

    public ArrayList<EventObject> findAllEventsWithName(String name)
    {
        ArrayList<EventObject> applicableEvents = new ArrayList<EventObject>();
        for(ArrayList<EventObject> _eventList : eventList.values())
        {
            for(EventObject _event : _eventList)
            {
                if(_event.getName().equals(name))
                {
                    applicableEvents.add(_event);
                }
            }
        }
        return applicableEvents;
    }

    private boolean checkClashesWithExistingEvents(LocalDate date, String name, RepeatType repeatType)
    {
        ArrayList<EventObject> applicableEvents = findAllEventsWithName(name);
        for(EventObject _event : applicableEvents)
        {
            if(event.getName().equals(name)) {return true;}
        }
        return false;
    }
    private boolean checkClashesWithExistingEvents(LocalDate date, String name, RepeatType repeatType, UUID uuidToIgnore)
    {
        ArrayList<EventObject> eventsOnDate = getEventsIncludingRepeats(date);
        for(EventObject event : eventsOnDate)
        {
            if(event.getName().equals(name) && !event.getUuid().equals(uuidToIgnore)) {return true;}
        }
        return false;
    }
    private boolean doThesePossiblyRepeatingEventsOverlap(EventObject e1, EventObject e2)
    {
        if(e1.getDate().equals(e2.getDate())) {return true;}

        /*
        EventObject earlier;
        EventObject later;
        if(e1.getDate().isBefore(e1.getDate()))
        {
            earlier = e1;
            later = e2;
        }
        else
        {
            earlier = e2;
            later = e1;
        }
        */

        if(e1.getRepeatType() == RepeatType.NONE && e2.getRepeatType() == RepeatType.NONE) {} // only overlap if same day
        if(e1.getRepeatType() == RepeatType.NONE && e2.getRepeatType() == RepeatType.DAILY) {} // overlap if e2 starts before e1
        if(e1.getRepeatType() == RepeatType.NONE && e2.getRepeatType() == RepeatType.WEEKLY) {} // overlap if e2 starts before e1 AND both are on same day of week
        if(e1.getRepeatType() == RepeatType.NONE && e2.getRepeatType() == RepeatType.MONTHLY) {} // overlap if e2 starts before e1 AND both are on same day of month
        if(e1.getRepeatType() == RepeatType.NONE && e2.getRepeatType() == RepeatType.YEARLY) {}

        if(e1.getRepeatType() == RepeatType.DAILY && e2.getRepeatType() == RepeatType.NONE) {} // overlap if e1 starts before e2
        if(e1.getRepeatType() == RepeatType.DAILY && e2.getRepeatType() == RepeatType.DAILY) {}
        if(e1.getRepeatType() == RepeatType.DAILY && e2.getRepeatType() == RepeatType.WEEKLY) {}
        if(e1.getRepeatType() == RepeatType.DAILY && e2.getRepeatType() == RepeatType.MONTHLY) {}
        if(e1.getRepeatType() == RepeatType.DAILY && e2.getRepeatType() == RepeatType.YEARLY) {}

        if(e1.getRepeatType() == RepeatType.WEEKLY && e2.getRepeatType() == RepeatType.NONE) {} // overlap if e1 starts before e2 AND both are on same day of week
        if(e1.getRepeatType() == RepeatType.WEEKLY && e2.getRepeatType() == RepeatType.DAILY) {}
        if(e1.getRepeatType() == RepeatType.WEEKLY && e2.getRepeatType() == RepeatType.WEEKLY) {}
        if(e1.getRepeatType() == RepeatType.WEEKLY && e2.getRepeatType() == RepeatType.MONTHLY) {}
        if(e1.getRepeatType() == RepeatType.WEEKLY && e2.getRepeatType() == RepeatType.YEARLY) {}

        if(e1.getRepeatType() == RepeatType.MONTHLY && e2.getRepeatType() == RepeatType.NONE) {}
        if(e1.getRepeatType() == RepeatType.MONTHLY && e2.getRepeatType() == RepeatType.DAILY) {}
        if(e1.getRepeatType() == RepeatType.MONTHLY && e2.getRepeatType() == RepeatType.WEEKLY) {}
        if(e1.getRepeatType() == RepeatType.MONTHLY && e2.getRepeatType() == RepeatType.MONTHLY) {}
        if(e1.getRepeatType() == RepeatType.MONTHLY && e2.getRepeatType() == RepeatType.YEARLY) {}

        if(e1.getRepeatType() == RepeatType.YEARLY && e2.getRepeatType() == RepeatType.NONE) {}
        if(e1.getRepeatType() == RepeatType.YEARLY && e2.getRepeatType() == RepeatType.DAILY) {}
        if(e1.getRepeatType() == RepeatType.YEARLY && e2.getRepeatType() == RepeatType.WEEKLY) {}
        if(e1.getRepeatType() == RepeatType.YEARLY && e2.getRepeatType() == RepeatType.MONTHLY) {}
        if(e1.getRepeatType() == RepeatType.YEARLY && e2.getRepeatType() == RepeatType.YEARLY) {}

        //
    }

    public void removeEvent(LocalDate date, String name)
    {
        // TODO: need to modify to take into account repeats - use uuid or somehow check for repeats with this name on this date

        if(eventList.containsKey(date))
        {
            ArrayList<EventObject> eventsOnDate = eventList.get(date);
            if(eventsOnDate == null) {return;}

            eventsOnDate.removeIf(e -> e.getName().equals(name) && e.getDate().equals(date));
            repeatingEventList.removeIf(e -> e.getName().equals(name) && e.getDate().equals(date));
        }
    }

    public int getEventCountIncludingRepeats(LocalDate date)
    {
        return getEventsIncludingRepeats(date).size();
    }
}
