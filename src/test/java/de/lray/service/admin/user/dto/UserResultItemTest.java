package de.lray.service.admin.user.dto;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

class UserResultItemTest extends AbstractDtoTest<UserResultItem> {

    static Stream<Arguments> potentialChangeActions() {
        return Stream.of(
                Arguments.of(Pair.<String, Function<UserResultItem, Object>>of(
                        "differing id", (changed) -> changed.id = "uk")),
                Arguments.of(Pair.<String, Function<UserResultItem, Object>>of(
                        "differing username", (changed) -> changed.userName = "uk")),
                Arguments.of(Pair.<String, Function<UserResultItem, Object>>of(
                        "differing name", (changed) -> changed.name = new UserName())),
                Arguments.of(Pair.<String, Function<UserResultItem, Object>>of(
                        "differing extension", (changed) -> changed.extension = new UserExtension())),
                Arguments.of(Pair.<String, Function<UserResultItem, Object>>of(
                        "differing email", (changed) -> changed.emails = Collections.emptyList())),
                Arguments.of(Pair.<String, Function<UserResultItem, Object>>of(
                        "differing phone", (changed) -> changed.phoneNumbers = Collections.emptyList())),
                Arguments.of(Pair.<String, Function<UserResultItem, Object>>of(
                        "differing roles", (changed) -> changed.roles = Collections.emptyList()))
        );
    }

    @Override
    protected UserResultItem newInstance() {
        return new UserResultItem();
    }

    protected UserResultItem fillDefaults(UserResultItem item) {
        item.id = "5";
        item.userName = "bla";
        item.name = new UserName("a", "b", "c");
        item.extension = new UserExtension(LocalDate.of(1986,4, 26));
        item.emails = List.of(new UserEmail());
        item.phoneNumbers = List.of(new UserPhone());
        item.roles = List.of("dev");
        return item;
    }
}
