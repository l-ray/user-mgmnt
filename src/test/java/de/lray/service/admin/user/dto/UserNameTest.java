package de.lray.service.admin.user.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;

class UserNameTest {

    @Test
    void handles_primitive_issues() {
        Assertions.assertThat(new UserName()).isEqualTo(new UserName());
        Assertions.assertThat(new UserName().equals(null)).isFalse();
    }

    @Test
    void when_similar_then_equal() {
        Assertions.assertThat(fillDefaults(new UserName()))
                .isEqualTo(fillDefaults(new UserName()));
    }

    @Test
    void when_differing_then_unequal() {
            var changes = Map.<String, Function<UserName, Object>>of(
                    "differing first", (changed) -> changed.givenName = "uk",
                    "differing middle", (changed) -> changed.middleName = "11880",
                    "differing given name", (changed) -> changed.familyName = "11880"
            );

            for (Map.Entry<String, Function<UserName, Object>> change : changes.entrySet()) {
                var changed = fillDefaults(new UserName());
                var action = change.getValue();
                action.apply(changed);
                Assertions.assertThat(fillDefaults(new UserName()))
                        .as(change.getKey())
                        .isNotEqualTo(changed);
            }
        }

    private UserName fillDefaults(UserName item) {
        item.givenName = "test";
        item.middleName = "test";
        item.familyName = "m@tro.se";
        return item;
    }
}