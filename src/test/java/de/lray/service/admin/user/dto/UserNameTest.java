package de.lray.service.admin.user.dto;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.provider.Arguments;

import java.util.function.Function;
import java.util.stream.Stream;

class UserNameTest extends AbstractDtoTest<UserName> {

    @Override
    protected UserName newInstance() {
        return new UserName();
    }

    static Stream<Arguments> potentialChangeActions() {
        return Stream.of(
                Arguments.of(Pair.<String, Function<UserName, Object>>of(
                        "differing first", (changed) -> changed.givenName = "uk")),
                Arguments.of(Pair.<String, Function<UserName, Object>>of(
                        "differing middle", (changed) -> changed.middleName = "11880")),
                Arguments.of(Pair.<String, Function<UserName, Object>>of(
                        "differing given name", (changed) -> changed.familyName = "11880"))
        );
    }

    protected UserName fillDefaults(UserName item) {
        item.givenName = "test";
        item.middleName = "test";
        item.familyName = "m@tro.se";
        return item;
    }
}