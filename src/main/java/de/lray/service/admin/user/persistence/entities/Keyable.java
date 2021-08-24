package de.lray.service.admin.user.persistence.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Table(indexes = @Index(name = "IDX_USER_PUBLIC_ID", columnList = "publicId"))
abstract public class Keyable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @Column(unique = true)
    private String publicId = "";

    private Date updateDate = new Date(0);
    private Date creationDate = new Date(0);

    public Long getId() {
        return id;
    }

    @PreUpdate
    @PrePersist
    public void prePersistDuties() {
        updateTimeStamps();
        generatePublicId();
    }

    public void updateTimeStamps() {
        updateDate = new Date();
        if (creationDate==null) {
            creationDate = new Date();
        }
    }

    public void generatePublicId() {
        if (publicId == null || publicId.isEmpty()) {
            publicId = UUID.randomUUID().toString();
        }
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
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
