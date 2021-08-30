package de.lray.service.admin.user.persistence.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Table(indexes = @Index(name = "IDX_USER_PUBLIC_ID", columnList = "publicId"))
public abstract class Keyable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("java:S1170")
    private final Long id = 0L;

    @Column(unique = true, nullable = false)
    private String publicId = "";
    @Column(columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private Date updateDate = new Date(0);
    @Column(columnDefinition = "DATE DEFAULT CURRENT_DATE")
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
}
