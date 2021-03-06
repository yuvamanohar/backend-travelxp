package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import utils.DateFormatter;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Created by yuva on 25/4/17.
 */
@MappedSuperclass
public abstract class BaseModel {

    private String createdAt ;
    private String updatedAt ;

    @JsonIgnore
    public Boolean softDeleted ;

    @PrePersist
    public void preCreate() {
        softDeleted = false ;
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

    @JsonIgnore
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonIgnore
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
