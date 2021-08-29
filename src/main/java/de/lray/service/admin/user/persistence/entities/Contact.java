package de.lray.service.admin.user.persistence.entities;

import de.lray.service.admin.user.validation.AgeRangeConstraint;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

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

    @AgeRangeConstraint
    private LocalDate birthDate;

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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
