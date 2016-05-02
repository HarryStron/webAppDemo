package com.cs391.data;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


@Entity
public class UserGroup implements Serializable {

    @Id
    private String sussexID;
    @NotNull
    private String groupname;

    public UserGroup() {
    }

    public UserGroup(String username, String groupName) {
        this.sussexID = username;
        this.groupname = groupName;
    }

    public String getSussexID() {
        return sussexID;
    }

    public void setSussexID(String sussexID) {
        this.sussexID = sussexID;
    }

    public String getGroupName() {
        return groupname;
    }

    public void setGroupName(String groupName) {
        this.groupname = groupName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.sussexID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserGroup other = (UserGroup) obj;
        if (!Objects.equals(this.sussexID, other.sussexID)) {
            return false;
        }
        return true;
    }

}
