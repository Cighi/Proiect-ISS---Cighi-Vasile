package org.iss.Domain;

import java.io.Serializable;

public class Identifiable<TID> implements Serializable {
    protected TID id;

    public TID getId() {
        return id;
    }

    public void setId(TID id) {
        this.id = id;
    }
}
