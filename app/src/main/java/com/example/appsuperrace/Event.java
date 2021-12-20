package com.example.appsuperrace;

import java.util.Objects;

public class Event {

    private int resourceid;
    private String name;

    public Event(int resourceid, String name) {
        this.resourceid = resourceid;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return resourceid == event.resourceid &&
                Objects.equals(name, event.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceid, name);
    }

    public int getResourceid() {
        return resourceid;
    }

    public void setResourceid(int resourceid) {
        this.resourceid = resourceid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
