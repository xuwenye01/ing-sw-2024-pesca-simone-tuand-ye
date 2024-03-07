package it.polimi.sw.client.model.corner;

import it.polimi.sw.client.model.resources.Object;
import it.polimi.sw.client.model.resources.Resource;

public class Corner {
    private Resource resource;
    private Object object;
    private boolean visible;
    private CornerLocationEnum location;
    private Corner linkedCorner;

    public Resource getResource() {
        return resource;
    }

    public boolean isVisible() {
        return visible;
    }

    public Corner getLinkedCorner() {
        return linkedCorner;
    }

    public Object getObject() {
        return object;
    }

    public CornerLocationEnum getLocation() {
        return location;
    }
    
}
