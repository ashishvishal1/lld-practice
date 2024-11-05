package com.example.Model;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.Listener.EventListener;

public class EventManager {
    private Map<String, List<EventListener> > listeners;

    public EventManager(String... events) {
        listeners = new HashMap<>();
        for(String event: events) {
            listeners.put(event, new ArrayList<>());
        }
    }

    public void addEvent(String... events) {
        for(String event: events) {
            listeners.put(event, new ArrayList<>());
        }
    }

    public void subscribe(String eventType, EventListener eventListener) throws Exception {
        if(this.listeners.containsKey(eventType)){
            this.listeners.get(eventType).add(eventListener);
        } else {
            throw new Exception("No such event type: " + eventType + " Exist.");
        }
    }

    public void unsubscribe(String eventType, EventListener eventListener) {
        if(this.listeners.containsKey(eventType)) {
            this.listeners.get(eventType).remove(eventListener);
        } else {
            System.out.printf("Event type {} doesn't exist\n", eventType);
        }
    }

    public void notify(String eventType, String fileName) {
        listeners.entrySet().stream().filter(entry -> entry.getKey().equals(eventType)).forEach(
            eventListeners -> eventListeners.getValue().stream().forEach(listener -> listener.update(eventType, fileName))
        );
    }

}