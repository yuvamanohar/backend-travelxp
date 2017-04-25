package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import utils.DateFormatter;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Created by yuva on 25/4/17.
 */
public abstract class BaseModel {
    public String createdAt ;
    public String updatedAt ;

    @JsonIgnore
    public Boolean softDeleted ;

    @PrePersist
    public void preCreate() {
        createdAt = DateFormatter.getReadableCurrentTime() ;
        updatedAt = createdAt ;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = DateFormatter.getReadableCurrentTime() ;
    }

    public void setSoftDeleted() {
        this.softDeleted = true ;
    }
}
