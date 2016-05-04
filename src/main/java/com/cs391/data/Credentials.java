package com.cs391.data;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Credentials implements Serializable{
    @Id
    String sussexID;
    @NotNull
    String pass;
    
    public String getSussexID() {
        return sussexID;
    }

    public String getPass() {
        return pass;
    }

    public void setSussexID(String sussexID) {
        this.sussexID = sussexID;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.sussexID);
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
        final Credentials other = (Credentials) obj;
        if (!Objects.equals(this.sussexID, other.sussexID)) {
            return false;
        }
        return true;
    }

    
}
