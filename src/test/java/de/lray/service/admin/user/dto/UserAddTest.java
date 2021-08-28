package de.lray.service.admin.user.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;

class UserAddTest {

    @Test
    @SuppressWarnings("java:S5838")
    void handles_primitive_issues() {
        Assertions.assertThat(new UserAdd()).isEqualTo(new UserAdd());
        Assertions.assertThat(new UserAdd()).isNotEqualTo(null);
        Assertions.assertThat(new UserAdd()).isNotEqualTo(new UserResource());
    }

    @Test
    void when_similar_then_equal() {
        Assertions.assertThat(fillDefaults(new UserAdd()))
                .isEqualTo(fillDefaults(new UserAdd()));
    }

    @Test
    void when_differing_then_unequal() {
            var changes = Map.<String, Function<UserAdd, Object>>of(
                    "differing password", (changed) -> changed.password = "!UK12345",
                    "differing super", (changed) -> changed.preferredLanguage = "uk"
            );

            for (Map.Entry<String, Function<UserAdd, Object>> change : changes.entrySet()) {
                var changed = fillDefaults(new UserAdd());
                var action = change.getValue();
                action.apply(changed);
                Assertions.assertThat(fillDefaults(new UserAdd()))
                        .as(change.getKey())
                        .isNotEqualTo(changed);
            }
        }

    private UserAdd fillDefaults(UserAdd item) {
        item.password = "DE0007!";
        item.preferredLanguage = "de";
        return item;
    }
}