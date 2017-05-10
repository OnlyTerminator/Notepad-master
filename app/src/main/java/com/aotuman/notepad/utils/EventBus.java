package com.aotuman.notepad.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aotuman on 2017/5/10.
 */

public class EventBus {
    private static EventBus mController;
    private LinkedHashMap<Integer,EventHandler> eventHandlers =
            new LinkedHashMap<Integer,EventHandler>();

    public interface EventType {
        final int CHANGEGROUP = 0;
    }

    private EventBus() {

    }

    public interface EventHandler {
        void onHandleEvent(int eventType, Object obj);
    }

    public static EventBus create() {
        if (mController == null) {
            mController = new EventBus();
        }
        return mController;
    }

    public void sendEvent(int eventType,  Object obj) {
        for (Iterator<Map.Entry<Integer, EventHandler>> handlers =
             eventHandlers.entrySet().iterator(); handlers.hasNext();) {
            Map.Entry<Integer, EventHandler> entry = handlers.next();
            EventHandler eventHandler = entry.getValue();
            if (eventHandler != null)
                eventHandler.onHandleEvent(eventType, obj);
        }
    }

    public void registerEventHandler(int key, EventHandler eventHandler) {
        synchronized (this) {
            eventHandlers.put(key, eventHandler);
        }
    }

    public void unRegisterEventHandler(int key) {
        synchronized (this) {
            if(eventHandlers.containsKey(key)) {
                eventHandlers.remove(key);
            }
        }
    }

}
