package de.lray.service.admin.user.persistence.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User extends Keyable {

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Credentials credentials;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    private Contact contact;

    private boolean systemUser;

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public boolean isSystemUser() {
        return systemUser;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
