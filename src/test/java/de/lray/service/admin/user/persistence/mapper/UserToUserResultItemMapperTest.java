package de.lray.service.admin.user.persistence.mapper;

import de.lray.service.admin.user.persistence.entities.Contact;
import de.lray.service.admin.user.persistence.entities.Credentials;
import de.lray.service.admin.user.persistence.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class UserToUserResultItemMapperTest {

    @Test
    void should_map_all_properties() {
        // Given
        var aBirthDate = LocalDate.of(2017,1,20);
        var aUser = new User();
        var aContact = new Contact();
        var aCredential = new Credentials();
        aContact.setFirstName("bla");
        aContact.setLastName("bla");
        aContact.setPrimaryEMail("lilow@nde.rs");
        aContact.setPhoneNumber("11833");
        aContact.setBirthDate(aBirthDate);

        aUser.setContact(aContact);
        aCredential.setUsername("manamana");
        aUser.setCredentials(aCredential);

        // When
        var result = UserToUserResultItemMapper.map(aUser);
        // Then
        Assertions.assertThat(result).hasFieldOrPropertyWithValue("userName","manamana");
        Assertions.assertThat(result.name)
                .hasFieldOrPropertyWithValue("familyName","bla")
                .hasFieldOrPropertyWithValue("givenName","bla");

        Assertions.assertThat(result.emails).hasSize(1).first()
                .hasFieldOrPropertyWithValue("value","lilow@nde.rs");

        Assertions.assertThat(result.phoneNumbers).hasSize(1).first()
                .hasFieldOrPropertyWithValue("value","11833");

        Assertions.assertThat(result.extension)
                .hasFieldOrPropertyWithValue("birthDate", aBirthDate);
    }

    @Test
    void should_be_resistant_to_null_values() {
        // Given
        var aUser = new User();
        aUser.setContact(new Contact());
        aUser.setCredentials(new Credentials());
        // When
        var result = UserToUserResultItemMapper.map(aUser);
        // Then
        Assertions.assertThat(result.emails).isEmpty();
        Assertions.assertThat(result.phoneNumbers).isEmpty();
    }

    @Test
    void should_fail_without_credentials() {
        // Given
        var aUser = new User();
        aUser.setContact(new Contact());
        // When / Then
        Assertions.assertThatThrownBy(() -> UserToUserResultItemMapper.map(aUser))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_fail_without_contact() {
        // Given
        var aUser = new User();
        aUser.setCredentials(new Credentials());
        // When / Then
        Assertions.assertThatThrownBy(() -> UserToUserResultItemMapper.map(aUser))
                .isInstanceOf(NullPointerException.class);
    }
}