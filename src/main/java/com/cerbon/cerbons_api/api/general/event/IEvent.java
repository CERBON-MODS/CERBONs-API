package com.cerbon.cerbons_api.api.general.event;

public interface IEvent {
    boolean shouldDoEvent();
    void doEvent();
    boolean shouldRemoveEvent();
    int tickSize();
}
