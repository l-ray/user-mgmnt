package de.lray.service.admin.user.dto;

import de.lray.service.admin.ScimTestMessageFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

class UserAddToUserMapperTest {
    @Test
    void should_map_all_properties() {
        // Given
        var aUser = ScimTestMessageFactory.createUserAdd();

        // When
        var result = UserAddToUserMapper.map(aUser);
        // Then
        Assertions.assertThat(result).hasFieldOrPropertyWithValue("publicId", ScimTestMessageFactory.EXAMPLE_ID);
        Assertions.assertThat(result.getContact())
                .hasFieldOrPropertyWithValue("lastName", aUser.name.familyName)
                .hasFieldOrPropertyWithValue("firstName", aUser.name.givenName)
                .hasFieldOrPropertyWithValue("birthDate", aUser.extension.birthDate)
                .hasFieldOrPropertyWithValue("primaryEMail", aUser.emails.get(0).value)
                .hasFieldOrPropertyWithValue("phoneNumber", aUser.phoneNumbers.get(0).value);
        Assertions.assertThat(result.getCredentials())
                .hasFieldOrPropertyWithValue("username", aUser.userName)
                .hasFieldOrPropertyWithValue("active", aUser.active);
        Assertions.assertThat(
                result.getCredentials().checkPassword(
                        ((ScimTestMessageFactory.UserAddWithPasswordGetter) aUser).getPassword())
        ).isTrue();
    }

    @ParameterizedTest
    @MethodSource("potentialNullFieldConstellations")
    void should_be_resistant_to_null_values(Pair<String, Function<UserAdd, Object>> aPair) {
        // Given
        var emptyUser = new UserAdd();
        aPair.getValue().apply(emptyUser);
        // When
        var result = UserAddToUserMapper.map(emptyUser);
        // Then
        Assertions.assertThat(result.getContact()).isNull();
        Assertions.assertThat(result.getCredentials()).isNull();
    }

    static Stream<Arguments> potentialNullFieldConstellations() {
        return Stream.<Arguments>of(
                Arguments.of(Pair.<String, Function<UserAdd, Object>>of(
                        "all null on main object", (changed) -> changed
                )),
                Arguments.of(Pair.<String, Function<UserAdd, Object>>of(
                        "all null on username", (changed) -> changed.name = new UserName()
                )),
                Arguments.of(Pair.<String, Function<UserAdd, Object>>of(
                        "all null on extension", (changed) -> changed.extension = new UserExtension()
                )),
                Arguments.of(Pair.<String, Function<UserAdd, Object>>of(
                        "first name null", (changed) -> {
                            changed.name = new UserName();
                            changed.name.givenName = "test";
                            return changed;
                        }
                )),
                Arguments.of(Pair.<String, Function<UserAdd, Object>>of(
                        "empty email list", (changed) -> changed.emails = List.of()
                )),
                Arguments.of(Pair.<String, Function<UserAdd, Object>>of(
                        "empty email", (changed) -> changed.emails = List.of(new UserEmail())
                )),
                Arguments.of(Pair.<String, Function<UserAdd, Object>>of(
                        "empty phone number list", (changed) -> changed.phoneNumbers = List.of()
                )),
                Arguments.of(Pair.<String, Function<UserAdd, Object>>of(
                        "empty phone number", (changed) -> changed.phoneNumbers = List.of(new UserPhone())
                ))
        );
    }

    @Test
    void should_be_resistant_to_null_value_in_extension() {
        // Given
        var emptyUser = new UserAdd();
        emptyUser.name = new UserName();
        emptyUser.name.familyName = "test";
        // When
        var result = UserAddToUserMapper.map(emptyUser);
        // Then
        Assertions.assertThat(result.getContact()).isNotNull();
        Assertions.assertThat(result.getCredentials()).isNull();
    }
}
