package com.driga.jstatscore.event.wrapper;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventWrapper extends Event implements Cancellable
{
    private static final HandlerList list;
    private boolean cancellable;
    
    public static HandlerList getHandlerList() {
        return EventWrapper.list;
    }
    
    public boolean isCancelled() {
        return this.cancellable;
    }
    
    public void setCancelled(boolean cancel) {
        this.cancellable = cancel;
    }
    
    public HandlerList getHandlers() {
        return EventWrapper.list;
    }
    
    public EventWrapper() {
        this.cancellable = false;
    }
    
    static {
        list = new HandlerList();
    }
}
