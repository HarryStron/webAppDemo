package com.cs391.data;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Log implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @NotNull
    String sussexID;
    @NotNull
    String eventDate;
    @NotNull
    String info;

    public void setSussexID(String sussexID) {
        this.sussexID = sussexID;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSussexID() {
        return sussexID;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Log other = (Log) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
}
