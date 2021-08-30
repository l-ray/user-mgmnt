package de.lray.service.admin.user.dto;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.function.Function;
import java.util.stream.Stream;

class UserResourceTest extends AbstractDtoTest<UserResource> {

    static Stream<Arguments> potentialChangeActions() {
        return Stream.of(
                Arguments.of(Pair.<String, Function<UserResource, Object>>of(
                        "differing language", (changed) -> changed.preferredLanguage = "uk")),
                Arguments.of(Pair.<String, Function<UserResource, Object>>of(
                        "differing locale", (changed) -> changed.locale = "uk")),
                Arguments.of(Pair.<String, Function<UserResource, Object>>of(
                        "differing displayname", (changed) -> changed.displayName = "wha")),
                Arguments.of(Pair.<String, Function<UserResource, Object>>of(
                        "differing super", (changed) -> changed.active = false))
        );
    }

    @Test
    void handles_inheritance_issue() {
        Assertions.assertThat(new UserResource()).isNotEqualTo(new UserResultItem());
    }

    @Override
    protected UserResource newInstance() {
        return new UserResource();
    }

    protected UserResource fillDefaults(UserResource item) {
        item.active = true;
        item.displayName = "test";
        item.preferredLanguage = "de";
        item.timezone = "UTC";
        item.locale = "de-DE";
        return item;
    }
}