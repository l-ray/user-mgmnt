package de.lray.service.admin.user.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;

class UserEmailTest {

    @Test
    void handles_primitive_issues() {
        Assertions.assertThat(new UserEmail()).isEqualTo(new UserEmail());
        Assertions.assertThat(new UserEmail().equals(null)).isFalse();
    }

    @Test
    void when_similar_then_equal() {
        Assertions.assertThat(fillDefaults(new UserEmail()))
                .isEqualTo(fillDefaults(new UserEmail()));
    }

    @Test
    void when_differing_then_unequal() {
            var changes = Map.<String, Function<UserEmail, Object>>of(
                    "differing type", (changed) -> changed.type = "uk",
                    "differing value", (changed) -> changed.value = "l@tzho.se",
                    "differing primary", (changed) -> changed.primary = false
            );

            for (Map.Entry<String, Function<UserEmail, Object>> change : changes.entrySet()) {
                var changed = fillDefaults(new UserEmail());
                var action = change.getValue();
                action.apply(changed);
                Assertions.assertThat(fillDefaults(new UserEmail()))
                        .as(change.getKey())
                        .isNotEqualTo(changed);
            }
        }

    private UserEmail fillDefaults(UserEmail item) {
        item.type = "test";
        item.display = "de";
        item.value = "m@tro.se";
        item.primary = true;
        return item;
    }
}