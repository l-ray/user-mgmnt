package de.lray.service.admin.user.dto;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.provider.Arguments;

import java.util.function.Function;
import java.util.stream.Stream;

class UserPhoneTest extends AbstractDtoTest<UserPhone> {

    static Stream<Arguments> potentialChangeActions() {
        return Stream.of(
                Arguments.of(Pair.<String, Function<UserPhone, Object>>of(
                        "differing type", (changed) -> changed.type = "uk")),
                Arguments.of(Pair.<String, Function<UserPhone, Object>>of(
                        "differing value", (changed) -> changed.value = "11880"))
        );
    }

    @Override
    protected UserPhone newInstance() {
        return new UserPhone();
    }

    protected UserPhone fillDefaults(UserPhone item) {
        item.type = "test";
        item.value = "m@tro.se";
        return item;
    }
}