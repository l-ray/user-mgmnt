package de.lray.service.admin.user.persistence.entities;

import jakarta.persistence.*;

import java.util.Date;

@MappedSuperclass
abstract public class Keyable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    private Date updateDate = new Date(0);
    private Date creationDate = new Date(0);

    public Long getId() {
        return id;
    }

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        updateDate = new Date();
        if (creationDate==null) {
            creationDate = new Date();
        }
    }
    public Date getCreationDate() {
        return creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
