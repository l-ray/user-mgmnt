package de.lray.service.admin.user.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;


@Entity
@Table(name = "contact")
public class Contact extends Keyable {

    @OneToOne(mappedBy = "contact")
    private User user;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Email
    private String primaryEMail;

    private String phoneNumber;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrimaryEMail() {
    return primaryEMail;
    }

    public void setPrimaryEMail(String email) {
        this.primaryEMail = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
