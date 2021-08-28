package de.lray.service.admin.user.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;

class UserPhoneTest {

    @Test
    @SuppressWarnings("java:S5838")
    void handles_primitive_issues() {
        Assertions.assertThat(new UserPhone()).isEqualTo(new UserPhone());
        Assertions.assertThat(new UserPhone()).isNotEqualTo(null);
    }

    @Test
    void when_similar_then_equal() {
        Assertions.assertThat(fillDefaults(new UserPhone()))
                .isEqualTo(fillDefaults(new UserPhone()));
    }

    @Test
    void when_differing_then_unequal() {
            var changes = Map.<String, Function<UserPhone, Object>>of(
                    "differing type", (changed) -> changed.type = "uk",
                    "differing value", (changed) -> changed.value = "11880"
            );

            for (Map.Entry<String, Function<UserPhone, Object>> change : changes.entrySet()) {
                var changed = fillDefaults(new UserPhone());
                var action = change.getValue();
                action.apply(changed);
                Assertions.assertThat(fillDefaults(new UserPhone()))
                        .as(change.getKey())
                        .isNotEqualTo(changed);
            }
        }

    private UserPhone fillDefaults(UserPhone item) {
        item.type = "test";
        item.value = "m@tro.se";
        return item;
    }
}