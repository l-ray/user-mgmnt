package de.lray.service.admin.user.dto;

import de.lray.service.admin.user.validation.AgeRangeConstraint;

import java.time.LocalDate;
import java.util.Objects;

public class UserExtension {
    public UserExtension() { }

    public UserExtension(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @AgeRangeConstraint
    public LocalDate birthDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserExtension that = (UserExtension) o;
        return Objects.equals(birthDate, that.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(birthDate);
    }
}
