package de.lray.service.admin.user.persistence.mapper;

import de.lray.service.admin.ScimTestMessageFactory;
import de.lray.service.admin.user.dto.UserAdd;
import de.lray.service.admin.user.dto.UserEmail;
import de.lray.service.admin.user.dto.UserName;
import de.lray.service.admin.user.dto.UserPhone;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class UserAddToUserMapperTest {
    @Test
    void should_map_all_properties() {
        // Given
        var aUser = ScimTestMessageFactory.createUserAdd();

        // When
        var result = UserAddToUserMapper.map(aUser);
        // Then
        Assertions.assertThat(result).hasFieldOrPropertyWithValue("publicId",ScimTestMessageFactory.EXAMPLE_ID);
        Assertions.assertThat(result.getContact())
                .hasFieldOrPropertyWithValue("lastName",aUser.name.familyName)
                .hasFieldOrPropertyWithValue("firstName",aUser.name.givenName)
                .hasFieldOrPropertyWithValue("primaryEMail",aUser.emails.get(0).value)
                .hasFieldOrPropertyWithValue("phoneNumber",aUser.phoneNumbers.get(0).value);
        Assertions.assertThat(result.getCredentials())
                .hasFieldOrPropertyWithValue("username",aUser.userName)
                .hasFieldOrPropertyWithValue("active",aUser.active);
    }

    @Test
    void should_be_resistant_to_null_values() {
        // Given
        var aUserList = Arrays.asList(
                new UserAdd(), new UserAdd(), new UserAdd(), new UserAdd(),new UserAdd(), new UserAdd(), new UserAdd()
        );
        aUserList.get(1).name = new UserName();
        aUserList.get(2).name = new UserName();
        aUserList.get(2).name.givenName = "test";
        aUserList.get(3).emails = List.of();
        aUserList.get(4).emails = List.of(new UserEmail());
        aUserList.get(5).phoneNumbers = List.of();
        aUserList.get(6).phoneNumbers = List.of(new UserPhone());

        for (UserAdd emptyUser: aUserList) {
            // When
            var result = UserAddToUserMapper.map(emptyUser);
            // Then
            Assertions.assertThat(result.getContact()).isNull();
            Assertions.assertThat(result.getCredentials()).isNull();
        }
    }
}
