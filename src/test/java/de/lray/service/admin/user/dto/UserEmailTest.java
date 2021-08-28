package de.lray.service.admin.user.dto;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.provider.Arguments;

import java.util.function.Function;
import java.util.stream.Stream;

class UserEmailTest extends AbstractDtoTest<UserEmail> {

    @Override
    protected UserEmail newInstance() {
        return new UserEmail();
    }

    static Stream<Arguments> potentialChangeActions() {
        return Stream.of(
                Arguments.of(Pair.<String, Function<UserEmail, Object>>of(
                        "differing type", (changed) -> changed.type = "uk")),
                Arguments.of(Pair.<String, Function<UserEmail, Object>>of(
                        "differing value", (changed) -> changed.value = "l@tzho.se")),
                Arguments.of(Pair.<String, Function<UserEmail, Object>>of(
                        "differing primary", (changed) -> changed.primary = false))
        );
    }

    protected UserEmail fillDefaults(UserEmail item) {
        item.type = "test";
        item.display = "de";
        item.value = "m@tro.se";
        item.primary = true;
        return item;
    }
}